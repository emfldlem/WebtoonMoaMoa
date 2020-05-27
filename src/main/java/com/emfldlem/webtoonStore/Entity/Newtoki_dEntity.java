package com.emfldlem.webtoonStore.Entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Table(name = "newtoki_d")
@Entity
public class Newtoki_dEntity implements Serializable {

    @Id
    @Column(name="title_d_no")
    String titleDNo;

    @Column(name="title_no")
    String titleNo;

    @Column(name="title_count")
    String titleCount;

    @Column(name="subject")
    String subject;

}
