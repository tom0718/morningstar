package com.mms.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity(name = "counseling")
@Table(name = "counseling")
public class Counseling {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq_no")
    private Long seqNo;
    @Column(name = "user_seq_no")
    private Long userSeqNo;
    private String register;
    private String updater;
    private String title;
    private String content;
    private String handling;
    @Column(name = "reg_date")
    private Date regDate;
    @Column(name = "update_date")
    private Date updateDate;
}
