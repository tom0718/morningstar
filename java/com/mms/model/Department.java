package com.mms.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity(name = "department")
@Table(name = "department")
public class Department {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String group;
    private String group2;
    private String group3;
    private Integer array;

}
