package com.mms.model;

import lombok.Data;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;

@Data
@Entity(name = "accountRole")
@Table(name = "account_role")
public class AccountRole {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq_no")
    private Long seqNo;
    @Column(name = "account_id")
    private String accountId;
    @Column(name = "department_id")
    private Long departmentId;
    private Boolean male;
    private Boolean female;

    @NotFound(action = NotFoundAction.IGNORE)
    @OneToOne()
    @JoinColumn(name = "department_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Department department;
}
