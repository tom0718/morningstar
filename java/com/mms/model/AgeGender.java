package com.mms.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity(name = "ageGender")
@Table(name = "age_gender")
public class AgeGender {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq_no")
    private Long seqNo;
    private String church;
    @Column(name = "department_seq_no")
    private Long departmentSeqNo;
    private Integer age;
    private String gender;
    private Long member;
}
