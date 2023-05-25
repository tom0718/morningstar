package com.mms.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity(name = "word")
@Table(name = "word")
public class Word {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq_no")
    private Long seqNo;
    private String type;
    private String title;
    @Column(name = "subTitle")
    private String subTitle;
    private String bible;
    private String contents;
    @Column(name = "service_date")
    private Date serviceDate;
    private String keyword; // :keyword: 형식

    @Column(name = "reg_date")
    private Date regDate;
    @Column(name = "mod_date")
    private Date modDate;

    private String register;
    private String updater;

}
