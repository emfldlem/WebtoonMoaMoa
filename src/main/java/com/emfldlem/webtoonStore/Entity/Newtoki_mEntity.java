package com.emfldlem.webtoonStore.Entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Table(name = "newtoki_m")
@Entity
public class Newtoki_mEntity implements Serializable {

    @Id
    @Column(name="title_no")
    String titleNo;

    @Column(name="title_nm")
    String titleNm;

    @Column(name="weekday")
    String weekday;

    @Column(name="initial_i")
    String initialI;

    @Column(name="genre")
    String genre;

    @Column(name= "update_term")
    String updateTerm;

    @Column(name="complete_yn")
    String completeYn;

    @Column(name="error_yn")
    String errorYn;

}
