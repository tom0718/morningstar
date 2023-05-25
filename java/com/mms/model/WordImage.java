package com.mms.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity(name = "wordImage")
@Table(name = "word_image")
public class WordImage {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq_no")
    private Long seqNo;
    @Column(name = "word_seq_no")
    private Long wordSeqNo;
    private String image;
    @Column(name = "file_name")
    private String fileName;

}
