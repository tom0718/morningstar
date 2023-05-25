package com.mms.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

public class AccountDTO {

	@Data
	public static class General{
		private String id;
		private String auth;
		private String part; // staff, pastor, admin, supreme, word
		private String code; // 교회코드
		private String name;
		private String password;
		private String phone;
		private String email;

		private Integer status;

		private String note;

		private List<Role> roleList = new ArrayList<>();
	}
	



	@Data
	public static class Role{

		private Long department;
		private String gender;

	}

	
}
