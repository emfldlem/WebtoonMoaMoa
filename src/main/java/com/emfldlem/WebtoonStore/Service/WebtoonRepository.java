package com.emfldlem.WebtoonStore.Service;

import com.emfldlem.WebtoonStore.Entity.WebtoonEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface WebtoonRepository extends CrudRepository<WebtoonEntity, String> {


    List<WebtoonEntity> findByWeekday(String weekday);


    int countByTitleId(String titleId);



}
