package com.emfldlem.WebtoonStore.Service;


import com.emfldlem.WebtoonStore.Entity.WebtoonEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WebtoonService {

    @Autowired
    WebtoonRepository webtoonRepository;

    public List<WebtoonEntity> getWeekList(String weekday) {
        return webtoonRepository.findByWeekday(weekday);
    }

    public int existTitle(String titleId){
        return webtoonRepository.countByTitleId(titleId);
    }


    public WebtoonEntity saveWebtoonEntity(WebtoonEntity webtoonEntity) {
        return webtoonRepository.save(webtoonEntity);
    }


   /* public String getLastBoardNo(String boardSe) {
        HotdealEntity empEntity = hotdealRepository.findFirstByBoardSeOrderByNoDesc(boardSe);
        return empEntity.getNo();
    }

    public List<HotdealEntity> getListHotdeal(String boardSe) {
        return hotdealRepository.findListByBoardSe(boardSe);
    }*/



}
