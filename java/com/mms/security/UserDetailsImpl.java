package com.mms.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.mms.model.Account;


@SuppressWarnings("serial")
public class UserDetailsImpl extends User{

	public UserDetailsImpl(Account account) {
		super(account.getId(), account.getPassword(), authorities(account));
	}

	private static Collection<? extends GrantedAuthority> authorities(Account account) {
		List<GrantedAuthority> authorities = new ArrayList<>();

		if(account.getPart().equals("supreme")){
			authorities.add(new SimpleGrantedAuthority("ROLE_SUPREME"));
		}else if(account.getPart().equals("admin")) {
			authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
		}else if(account.getPart().equals("staff")){
			authorities.add(new SimpleGrantedAuthority("ROLE_STAFF"));
		}else if(account.getPart().equals("pastor")){
			authorities.add(new SimpleGrantedAuthority("ROLE_PASTOR"));
		}else if(account.getPart().equals("word")){
			authorities.add(new SimpleGrantedAuthority("ROLE_WORD"));
		}else if(account.getPart().equals("manager")){
			authorities.add(new SimpleGrantedAuthority("ROLE_MANAGER"));
		}
		
		return authorities;
	}
	
	
	
}
