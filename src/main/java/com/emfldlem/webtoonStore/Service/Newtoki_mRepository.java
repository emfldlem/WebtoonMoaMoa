package com.emfldlem.webtoonStore.Service;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.emfldlem.webtoonStore.Entity.Newtoki_mEntity;

public interface Newtoki_mRepository extends CrudRepository<Newtoki_mEntity, String> {

    List<Newtoki_mEntity> findAll();

    List<Newtoki_mEntity> findByCompleteYn(String completeYn);
    List<Newtoki_mEntity> findTop5ByCompleteYnAndErrorYn(String completeYn, String errorYn);
    Newtoki_mEntity findByTitleNo(String titleNo);

}
