package com.mms.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Entity(name="genderCount")
@Table(name="age_gender")
public class GenderCount implements Serializable{
    @Id
    @Column(name = "seq_no")
    private Long seqNo;
    private String gender;
    private Integer count;
}