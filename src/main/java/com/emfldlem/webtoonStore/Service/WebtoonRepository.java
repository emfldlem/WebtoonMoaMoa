package com.emfldlem.webtoonStore.Service;

import com.emfldlem.webtoonStore.Entity.WebtoonEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface WebtoonRepository extends CrudRepository<WebtoonEntity, String> {


    List<WebtoonEntity> findByWeekday(String weekday);


    int countByTitleId(String titleId);



}
