package com.mms.model;

import lombok.Data;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity(name = "rentRoom")
@Table(name = "rent_room")
public class RentRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq_no")
    private Long seqNo;
    private Long department;
    private String gender;
    private String room;
    @Column(name = "rent_date")
    private LocalDate rentDate;
    @Column(name = "start_time")
    private LocalTime startTime;
    @Column(name = "end_time")
    private LocalTime endTime;
    private String purpose;
    private String register;
    private LocalDateTime regDatetime;
    @Column(name = "different_time")
    private Integer differentTime;
    private String status; // active, completed, cancel

    @NotFound(action = NotFoundAction.IGNORE)
    @OneToOne()
    @JoinColumn(name = "department", referencedColumnName = "id", insertable = false, updatable = false)
    private Department depart;

    @OneToMany
    @JoinColumn(name = "rent_room_seq_no", referencedColumnName = "seq_no", insertable = false, updatable = false)
    @Where(clause = "type = 'start'")
    @OrderBy("rentTime asc")
    private List<RentTime> startTimeList = new ArrayList<>();

    @OneToMany
    @JoinColumn(name = "rent_room_seq_no", referencedColumnName = "seq_no", insertable = false, updatable = false)
    @Where(clause = "type = 'end'")
    @OrderBy("rentTime asc")
    private List<RentTime> endTimeList = new ArrayList<>();
}
