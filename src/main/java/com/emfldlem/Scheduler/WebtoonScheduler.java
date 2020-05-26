package com.emfldlem.Scheduler;

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
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.emfldlem.Common.CommonUtil;
import com.emfldlem.webtoonStore.Entity.WebtoonEntity;
import com.emfldlem.webtoonStore.Service.WebtoonService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class WebtoonScheduler {

    @Value("${naver.detail.url}")
    String naverDetailUrl;

    @Value("${naver.list.url}")
    String naverListUrl;

    @Value("${naver.title.list}")
    String naverTitleList;


    @Autowired
    WebtoonService webtoonService;

    //매일 새벽 1시
    //@Scheduled(cron = "* *  1  *  * ?")
    void naverWebtoonDown() {
        try {

            //오늘 날짜 별로 week 구하기
            String dayOfWeek = CommonUtil.getDayOfWeek();

            List<WebtoonEntity> weekWebtoonList = webtoonService.getWeekList(dayOfWeek);

            for(WebtoonEntity webtoon : weekWebtoonList) {

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

        }
        catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    //매월 말일 새벽 2시
    //@Scheduled(cron = "0 0 3 30 1/1 ?")
    void naverWebtoonListUpdate() {
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
                if(!StringUtils.isEmpty(titleNm)) {
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

        }
        catch (Exception e) {
            log.error(e.getMessage());
        }
    }

}
