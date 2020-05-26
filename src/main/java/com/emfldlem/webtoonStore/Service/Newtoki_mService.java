package com.emfldlem.webtoonStore.Service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.emfldlem.webtoonStore.Entity.Newtoki_mEntity;

@Service
public class Newtoki_mService {

    @Autowired
    Newtoki_mRepository newtoki_mRepository;

    public List<Newtoki_mEntity> getList() {
        return newtoki_mRepository.findAll();
    }

}
