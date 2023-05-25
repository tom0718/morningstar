package com.mms.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity(name = "providenceFamily")
@Table(name = "providence_family")
public class ProvidenceFamily {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq_no")
    private Long seqNo;
    @Column(name = "user_seq_no")
    private Long userSeqNo;
    private String title;
    private String name;

}
