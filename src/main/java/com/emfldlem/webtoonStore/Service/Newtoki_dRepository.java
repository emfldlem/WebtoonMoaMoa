package com.emfldlem.webtoonStore.Service;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.emfldlem.webtoonStore.Entity.Newtoki_dEntity;

public interface Newtoki_dRepository extends CrudRepository<Newtoki_dEntity, String> {


    List<Newtoki_dEntity> findByTitleNo(String titleNo);

    Newtoki_dEntity findTopByTitleNoOrderByTitleDNoDesc(String titleNo);


}
