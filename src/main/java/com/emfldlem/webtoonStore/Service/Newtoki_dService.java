package com.emfldlem.webtoonStore.Service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.emfldlem.webtoonStore.Entity.Newtoki_dEntity;

@Service
public class Newtoki_dService {

    @Autowired
    Newtoki_dRepository newtoki_dRepository;

    public List<Newtoki_dEntity> getTitleNoList(String titleNo) {
        return newtoki_dRepository.findByTitleNo(titleNo);
    }

    public Newtoki_dEntity saveWebtoonEntity(Newtoki_dEntity newtoki_dEntity) {
        return newtoki_dRepository.save(newtoki_dEntity);
    }

}
