package com.mms.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity(name = "family")
@Table(name = "family")
public class Family {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq_no")
    private Long seqNo;
    @Column(name = "user_seq_no")
    private Long userSeqNo;
    private String title;
}
