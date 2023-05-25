package com.mms.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Entity(name = "rentTime")
@Table(name = "rent_time")
public class RentTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq_no")
    private Long seqNo;
    private String user;
    @Column(name = "rent_room_seq_no")
    private Long rentRoomSeqNo;
    private String type; // start, end
    private String room;
    @Column(name = "rent_date")
    private LocalDate rentDate;
    @Column(name = "rent_time")
    private LocalTime rentTime;
    private Boolean temp;
}
