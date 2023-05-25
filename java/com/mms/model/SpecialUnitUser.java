package com.mms.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity(name = "specialUnitUser")
@Table(name = "special_unit_user")
public class SpecialUnitUser {

    @Id
    private Long seqNo;
    private String church;
    private String unit;
    private Integer count;

}
