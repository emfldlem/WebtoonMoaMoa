package com.emfldlem.webtoonStore.Entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Table(name = "nv_mgmt")
@Entity
public class WebtoonEntity implements Serializable {

    @Id
    @Column(name="title_id")
    String titleId;

    @Column(name="title_nm")
    String titleNm;

    @Column(name="no")
    String no;

    @Column(name="weekday")
    String weekday;


}
