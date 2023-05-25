package com.mms.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity(name = "jobUser")
@Table(name = "job_user")
public class JobUser {

    @Id
    private Long seqNo;
    private String church;
    private String job;
    private Integer count;

}
