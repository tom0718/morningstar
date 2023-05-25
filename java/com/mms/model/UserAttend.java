package com.mms.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity(name = "userAttend")
@Table(name = "user_attend")
public class UserAttend {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq_no")
    private Long seqNo;
    private String type; // dawn, wednesday, sunday
    @Column(name = "attend_type")
    private String attendType; // online, mainChurch, anotherChurch, etc
    @Column(name = "user_seq_no")
    private Long userSeqNo;
    private Long department;
    private String gender;
    @Column(name = "attend_date")
    private LocalDate attendDate;
    @Column(name = "attend_year")
    private Integer attendYear;
    @Column(name = "attend_month")
    private Integer attendMonth;
    private Long register;
    private String reason;
    @Column(name = "reg_datetime")
    private LocalDateTime regDatetime;

}
