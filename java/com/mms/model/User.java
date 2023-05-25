package com.mms.model;

import com.mms.model.converter.MobileConverter;
import lombok.Data;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity(name = "user")
@Table(name = "user")
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    private String blood; // 혈액형
    @Column(name = "department_seq_no")
    private Long departmentSeqNo; // 부서

    @Column(name = "completion_date")
    private Date completionDate; // 수료일

    @Convert(converter = MobileConverter.class)
    private String phone;

    private String mission; // 사명
    @Column(name = "religious_level")
    private String religiousLevel; // 신급
    @Column(name = "second_generation")
    private Integer secondGeneration; // 2세여부 1:가정국2세, 2:장년부2세, 3:2세아님.
    private String star; // 스타
    @Column(name = "special_unit")
    private String specialUnit; // 특수부서
    private Integer theology; // 신학기수
    @Column(name = "earlier_church")
    private String earlierChurch; // 이전교회
    @Column(name = "transfer_date")
    private Date transferDate; // 전입일

    private String school; // 졸업학교
    private String major; // 전공
    private String job; // 직업
    private String company; // 회사명
    private String talent; // 특기
    private String feature; // 특징

    private String address; // 주소
    private String credit; // 공적
    private String note; // 비고

    @Column(name = "reg_date")
    private Date regDate;
    @Column(name = "modi_date")
    private Date modiDate;

    private String register;
    private String updater;


    private Boolean deleted = false;
    @Column(name = "deleted_date")
    private Date deletedDate;

    @Transient
    private Integer age;

    @OneToOne()
    @JoinColumn(name = "department_seq_no", referencedColumnName = "id", insertable = false, updatable = false)
    private Department department;

    @OneToMany()
    @JoinColumn(name = "user", referencedColumnName = "seq_no", insertable = false, updatable = false)
    private List<License> licenseList = new ArrayList<>();

    @OneToMany()
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "user_seq_no", referencedColumnName = "seq_no", insertable = false, updatable = false)
    private List<Family> familyList = new ArrayList<>();

    @OneToMany()
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "user_seq_no", referencedColumnName = "seq_no", insertable = false, updatable = false)
    private List<ProvidenceFamily> providenceList = new ArrayList<>();

}
