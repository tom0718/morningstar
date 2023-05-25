package com.mms.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import com.mms.model.converter.MobileConverter;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import lombok.Data;


@Data
@Entity(name="account")
@Table(name = "account")
public class Account implements Serializable{
	
	private static final long serialVersionUID = 5733981698824324909L;
	
	@Id
	private String id;
	private String auth;
	private String code;
	private String part; // staff, pastor, admin, supreme, word, manager
	private String name;
	private String password;

	@Convert(converter = MobileConverter.class)
	private String phone;
	private String email;

	private String note;

	private Integer fail = 0; // 로그인 실패 횟수

	private Integer status; // 1:사용중, 2:비번잠김, 3:사용중지

	private Date joined;

	@Column(name = "update_date")
	private Date updateDate;

	private Date last; // 마지막 로그인시간
	
	private boolean deleted = false;
	
	@Transient
	private String resPhone;
	@Transient
	private String strDate;

	@NotFound(action = NotFoundAction.IGNORE)
	@OneToOne()
	@JoinColumn(name = "code", referencedColumnName = "code", insertable = false, updatable = false)
	private Code church;

	@NotFound(action = NotFoundAction.IGNORE)
	@OneToMany()
	@OrderBy("seqNo asc")
	@JoinColumn(name = "account_id", referencedColumnName = "id", insertable = false, updatable = false)
	private List<AccountRole> accountRoleList = new ArrayList<>();

	
}
