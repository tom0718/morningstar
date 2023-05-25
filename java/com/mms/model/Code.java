package com.mms.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity(name = "code")
@Table(name = "code")
public class Code {

    @Id
    private String code;
    private String name;
}
