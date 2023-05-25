package com.mms.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.mms.model.Account;
import com.mms.repository.AccountRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{

	@Autowired
	private AccountRepository accountRepositoryA;
	
	 @Override
	public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
		 
		 Optional<Account> account = accountRepositoryA.findByIdAndStatusAndFailLessThan(id, 1, 6);
		 
		 if(!account.isPresent()){
			 throw new UsernameNotFoundException(id);
		 }
		return new UserDetailsImpl(account.get());
		
	}
	
}
