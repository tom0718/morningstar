package com.mms.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity(name = "relation")
@Table(name = "relation")
public class Relation {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq_no")
    private Long seqNo;
    private Long me;
    private Long family;
    private String name;

    @OneToOne()
    @JoinColumn(name = "family", referencedColumnName = "seq_no", insertable = false, updatable = false)
    private User relation;

}
