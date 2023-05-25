package com.mms.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity(name = "starUser")
@Table(name = "star_user")
public class StarUser {

    @Id
    private Long seqNo;
    private String church;
    private String star;
    private Integer count;

}
