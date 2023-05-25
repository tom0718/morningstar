package com.mms.model;

import lombok.Data;

@Data
public class RentRoomDTO {

    private Long seqNo;
    private Long department;
    private Boolean male;
    private Boolean female;
    private String room;
    private String rentDate;
    private String startTime;
    private String endTime;
    private String purpose;
}
