package com.emfldlem.webtoonStore.Controller;


import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import javax.imageio.ImageIO;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.emfldlem.Common.CommonUtil;
import com.emfldlem.webtoonStore.Entity.Newtoki_dEntity;
import com.emfldlem.webtoonStore.Entity.Newtoki_mEntity;
import com.emfldlem.webtoonStore.Entity.WebtoonEntity;
import com.emfldlem.webtoonStore.Service.Newtoki_dService;
import com.emfldlem.webtoonStore.Service.Newtoki_mService;
import com.emfldlem.webtoonStore.Service.WebtoonService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping(value = "/webtoon")
public class webtoonStoreController {

    @Value("${naver.detail.url}")
    String naverDetailUrl;

    @Value("${naver.list.url}")
    String naverListUrl;

    @Value("${naver.title.list}")
    String naverTitleList;


    @Autowired
    WebtoonService webtoonService;

    @Autowired
    Newtoki_dService newtoki_dService;

    @Autowired
    Newtoki_mService newtoki_mService;


    @GetMapping(value = "/test")
    @ResponseBody
    public void webtoonTest() {

        try {

            //오늘 날짜 별로 week 구하기
            String dayOfWeek = CommonUtil.getDayOfWeek();

            List<WebtoonEntity> weekWebtoonList = webtoonService.getWeekList(dayOfWeek);

            for (WebtoonEntity webtoon : weekWebtoonList) {

                //해당 webtoon의 가장 최신화 조회
                String listUrl = naverListUrl + "titleId=" + webtoon.getTitleId();
                Document listDoc = Jsoup.connect(listUrl).get();
                Elements listElemList = listDoc.select(".viewList tr:not(.band_banner) a");
                String lastNo = (listElemList.get(0).attributes().get("href").split("no=")[1]).split("&")[0];

                int currentNo = Integer.parseInt(webtoon.getNo());

                log.info(webtoon.getTitleNm() + " 최신화는 " + lastNo + "화");

                while (Integer.parseInt(lastNo) >= currentNo) {

                    //폴더 생성
                    String folderDir = "E:\\03.만화\\웹툰\\" + webtoon.getTitleNm() + "\\" + currentNo;
                    File filedir = new File(folderDir);
                    if (!filedir.isDirectory()) {
                        filedir.mkdirs();
                    }

                    String url = naverDetailUrl + "titleId=" + webtoon.getTitleId() + "&no=" + currentNo;
                    Document doc = Jsoup.connect(url).get();
                    Elements elemList = doc.select(".wt_viewer img");

                    int index = 1;
                    for (Element anElem : elemList) {

                        File imgFile = new File(folderDir + "\\" + currentNo + "_" + index + ".jpg");

                        URL imgUrl = new URL(anElem.attributes().get("src"));


                        URLConnection urlConnection = imgUrl.openConnection();
                        urlConnection.setRequestProperty("referer", "https://comic.naver.com");

                        urlConnection.connect();

                        InputStream in = urlConnection.getInputStream();
                        BufferedImage bi = ImageIO.read(in);
                        ImageIO.write(bi, "jpg", imgFile);
                        log.info(webtoon.getTitleNm() + " " + currentNo + "화 " + index + "번째 파일 다운 완료");

                        index++;

                    }
                    currentNo++;
                }

                log.info(webtoon.getTitleNm() + " " + currentNo + "화까지 저장");
                webtoon.setNo(String.valueOf(currentNo));
                webtoonService.saveWebtoonEntity(webtoon);

            }

        } catch (Exception e) {
            log.error(e.getMessage());
        }

    }


    @GetMapping(value = "/list")
    @ResponseBody
    public void webtoonList() {

        try {
            String titleListUrl = naverTitleList;
            Document listDoc = Jsoup.connect(titleListUrl).get();
            Elements listElemList = listDoc.select(".col_inner ul li a");

            for (Element anElem : listElemList) {
                anElem.attributes().get("href");

                String[] arrHref = anElem.attributes().get("href").split("=");
                String titleId = arrHref[1].split("&")[0];
                String weekday = arrHref[2];

                String titleNm = anElem.attributes().get("title");
                if (!StringUtils.isEmpty(titleNm)) {
                    WebtoonEntity webtoonEntity = new WebtoonEntity();
                    webtoonEntity.setTitleId(titleId);
                    webtoonEntity.setWeekday(weekday);
                    webtoonEntity.setTitleNm(titleNm);
                    webtoonEntity.setNo("1");

                    log.info("{}", webtoonEntity);

                    if (webtoonService.existTitle(titleId) == 0) {
                        webtoonService.saveWebtoonEntity(webtoonEntity);
                        log.info("신규 웹툰을 리스트에 추가 하였습니다. {}", webtoonEntity);
                    } else {
                        log.info("기존에 존재하는 웹툰 입니다. {}", webtoonEntity);
                    }
                }

            }

        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @GetMapping(value = "/newtoki")
    @ResponseBody
    public void newtoki() {

        try {
            String titleListUrl = "https://newtoki62.com/webtoon/p";
            for (int i = 1; i <= 10; i++) {
                Document listDoc = Jsoup.connect(titleListUrl + i+"?toon=BL%2FGL").userAgent("Mozilla").get();

                Elements listElemList = listDoc.select("#webtoon-list-all li .img-item div a");
                Elements updateDateElemList = listDoc.select("#webtoon-list-all li .list-date");
                Elements liElemList = listDoc.select("#webtoon-list-all li");
                for(int j=0; j <liElemList.size(); j++) {
                    Element aElem = listElemList.get(j);
                    Element udDateElem = updateDateElemList.get(j);
                    Element liElem = liElemList.get(j);

                    String[] arrHref = aElem.attributes().get("href").split("/");

                    Newtoki_mEntity newtoki_mEntity = newtoki_mService.findByTitleNo(arrHref[4]);
                    if(ObjectUtils.isEmpty(newtoki_mEntity)) {
                       newtoki_mEntity = new Newtoki_mEntity();
                       newtoki_mEntity.setTitleNo(arrHref[4]);
                       newtoki_mEntity.setTitleNm(liElem.attributes().get("date-title"));
                       newtoki_mEntity.setCompleteYn("N");
                       newtoki_mEntity.setErrorYn("N");
                    }
                    newtoki_mEntity.setWeekday(liElem.attributes().get("data-weekday"));
                    newtoki_mEntity.setInitialI(liElem.attributes().get("data-initial"));
                    newtoki_mEntity.setGenre(liElem.attributes().get("data-genre")+",BL");
                    newtoki_mEntity.setTitleNm(liElem.attributes().get("date-title"));
                    newtoki_mEntity.setCompleteYn("Y");
                    newtoki_mEntity.setUpdateTerm(udDateElem.text());

                    newtoki_mService.saveNewtokiM(newtoki_mEntity);
                    log.error("{}",newtoki_mEntity);

                }

            }

        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

   // @Scheduled(cron = "0 0/1 * * * ?")
    /*@GetMapping(value = "/newtoki_d")
    @ResponseBody*/
    public void newtoki_d() {
        Newtoki_mEntity errorNewtoki = new Newtoki_mEntity();

        try {

            List<Newtoki_mEntity> MList = newtoki_mService.getList();

            for (Newtoki_mEntity MnewtoKi : MList) {
                errorNewtoki = MnewtoKi;

                String titleListUrl = "https://newtoki62.com/webtoon/";
                Document listDoc = Jsoup.connect(titleListUrl + MnewtoKi.getTitleNo()).userAgent("Mozilla").get();
                Elements listElemList = listDoc.select("#serial-move ul li.list-item div.wr-subject a");
                Elements reversList = new Elements();

                while(!listElemList.isEmpty()) {
                    reversList.add(listElemList.last());
                    listElemList.remove(listElemList.size()-1);
                }

                Newtoki_dEntity lastD = newtoki_dService.findTopByTitleNoOrderByTitleDNo(MnewtoKi.getTitleNo());

                for (Element anElem : reversList) {
                    String[] arrHref = anElem.attributes().get("href").split("/");

                    String detailUrl = "https://newtoki62.com/webtoon/" + arrHref[4];
                    String detailSubject = arrHref[5].substring(0, arrHref[5].indexOf("?"));

                    if (ObjectUtils.isEmpty(lastD) || Integer.parseInt(lastD.getTitleDNo()) <= Integer.parseInt(arrHref[4])) {

                        Document doc = Jsoup.connect(detailUrl).userAgent("Mozilla").get();
                        Elements elemList = doc.select(".view-content img");

                        int index = 0;
                        String folderDir = "E:\\03.만화\\웹툰\\뉴토끼\\" + MnewtoKi.getTitleNm() + "\\" + detailSubject;
                        File filedir = new File(folderDir);

                        if (!filedir.isDirectory()) {
                            filedir.mkdirs();
                        }

                        for (Element anElem_d : elemList) {
                            String imgSrc = anElem_d.attributes().get("data-original");

                            File imgFile = new File(folderDir + "\\" + index + ".jpg");

                            if (!imgFile.isFile()) {
                                URL imgUrl = new URL(imgSrc);


                                URLConnection urlConnection = imgUrl.openConnection();
                                urlConnection.setRequestProperty("referer", "https://newtoki62.com");
                                urlConnection.addRequestProperty("User-Agent", "Mozilla");

                                urlConnection.connect();

                                InputStream in = urlConnection.getInputStream();
                                BufferedImage bi = ImageIO.read(in);
                                ImageIO.write(bi, "jpg", imgFile);

                                log.error("생성완료============" + folderDir + "\\" + index + ".jpg");

                                index++;
                            }
                        }
                        Newtoki_dEntity newtoki_dEntity = new Newtoki_dEntity();
                        newtoki_dEntity.setTitleNo(MnewtoKi.getTitleNo());
                        newtoki_dEntity.setTitleDNo(arrHref[4]);
                        newtoki_dEntity.setSubject(arrHref[5].substring(0,arrHref[5].indexOf("?")));

                        newtoki_dService.saveWebtoonEntity(newtoki_dEntity);
                    }
                }
                MnewtoKi.setCompleteYn("Y");
                newtoki_mService.saveNewtokiM(MnewtoKi);

            }

        } catch (Exception e) {
            log.error(e.getMessage());
            errorNewtoki.setErrorYn("Y");
            newtoki_mService.saveNewtokiM(errorNewtoki);
        }
    }


}
