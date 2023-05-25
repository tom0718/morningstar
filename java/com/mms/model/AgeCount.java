package com.mms.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity(name="ageCount")
@Table(name="age_count")
public class AgeCount {

    @Id
    private String church;
    private Integer baby;
    private Integer ten;
    private Integer twenty;
    private Integer thirty;
    private Integer forty;
    private Integer fifty;
    private Integer sixty;
    private Integer seventy;
    private Integer eighty;
    private Integer ninety;
    private Integer hundred;

}