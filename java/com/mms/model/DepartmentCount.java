package com.mms.model;

import lombok.Data;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity(name="departmentCount")
@Table(name="age_gender")
public class DepartmentCount implements Serializable {
    @Id
    @Column(name = "seq_no")
    private Long seqNo;
    @Column(name = "department_seq_no")
    private Long departmentSeqNo;
    private Integer count; // 총원

    @Transient
    private Integer attendCount; // 예배참석 인원

    @Transient
    private Float attendRate; // 출석율

    @NotFound(action = NotFoundAction.IGNORE)
    @OneToOne()
    @JoinColumn(name = "department_seq_no", referencedColumnName = "id", insertable = false, updatable = false)
    private Department department;
}
