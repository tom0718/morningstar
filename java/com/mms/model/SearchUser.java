package com.mms.model;

import com.mms.model.converter.MobileConverter;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity(name = "searchUser")
@Table(name = "user")
public class SearchUser implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq_no")
    private Long seqNo;
    private String secure;
    private String code; // 교회코드
    @Column(name = "real_name")
    private String realName; // 본명
    @Column(name = "re_name")
    private String reName; // 개명

    private String thumbnail;
    private Date birthday; // 생일
    private String gender; // 성별
    @Column(name = "department_seq_no")
    private Long departmentSeqNo; // 부서

    @Column(name = "completion_date")
    private Date completionDate; // 수료일

    @Convert(converter = MobileConverter.class)
    private String phone;

    private Boolean deleted = false;

    @Transient
    private Integer age;

    @Transient
    private String strPhone;

    @OneToOne()
    @JoinColumn(name = "department_seq_no", referencedColumnName = "id", insertable = false, updatable = false)
    private Department department;


}
