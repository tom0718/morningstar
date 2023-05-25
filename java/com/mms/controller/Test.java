package com.mms.controller;

import com.mms.util.FunctionUtil;
import com.mms.util.SecureUtil;
import org.apache.commons.lang3.ObjectUtils;

import java.time.LocalTime;

public class Test extends FunctionUtil{

	
	
	public static void main(String[] args) throws Exception {

		String id = "tom0718";
		String res = "";

		if (ObjectUtils.isNotEmpty(id)) {
			StringBuilder stringBuilder = new StringBuilder(id);
			res = stringBuilder.replace(2, 4, "**").toString();
		}

		String secure = setID.apply("");
		String code = SecureUtil.encryptMobileNumber("21050345498207");

		//System.out.println(KeyGenerators.string().generateKey());

		System.out.println(SecureUtil.decryptMobileNumber("zMhcJrB5LvLUfHBe3482caygh2EvPIIqrlUY7GwAalZDO7IF9qL9gJzv6fDeOLx7Unv4127+iPAaIgnd+A26JQ=="));

		/*
		String content = "동해물과 백두산이 마르고 닳도록 하나님이 보우하사 우리나라 만세. 하나님을 사랑하세";
		StringBuffer sb = new StringBuffer();
		sb.append("<span class=\"red\">");
		sb.append("하나님");
		sb.append("</span>");

		String newContent = content.replaceAll("하나님", sb.toString());

		//System.out.println(newContent);


		List<AccountRole> list = new ArrayList<>();

		AccountRole role1 = new AccountRole();
		role1.setSeqNo(1l);
		role1.setAccountId("tommy");
		role1.setDepartmentId(1l);
		role1.setMale(true);
		role1.setFemale(false);

		list.add(role1);

		AccountRole role2 = new AccountRole();
		role2.setSeqNo(2l);
		role2.setAccountId("tommy");
		role2.setDepartmentId(1l);
		role2.setMale(false);
		role2.setFemale(true);

		list.add(role2);

		AccountRole role3 = new AccountRole();
		role3.setSeqNo(1l);
		role3.setAccountId("tommy");
		role3.setDepartmentId(1l);
		role3.setMale(true);
		role3.setFemale(false);

		list.add(role3);

		//list.stream().distinct(r -> r.getDepartmentId()).forEach(r -> System.out.println(r.getDepartmentId()));

		 */

		LocalTime time1 = LocalTime.of(12, 0, 0);
		LocalTime time2 = LocalTime.of(13, 30, 0);

		boolean flag = true;

		do{
			System.out.println("time ===> " + time1);
			time1 = time1.plusMinutes(30);

			flag = !time1.isAfter(time2);

		}while(flag);


		//System.out.println("different time ==> " + ChronoUnit.MINUTES.between(time1, time2));

	}

}
