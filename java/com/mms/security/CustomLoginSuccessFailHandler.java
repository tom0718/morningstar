package com.mms.security;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import com.mms.model.Account;
import com.mms.repository.AccountRepository;
import com.mms.util.FunctionUtil;

public class CustomLoginSuccessFailHandler extends FunctionUtil implements AuthenticationFailureHandler{
	
	@Autowired
	private AccountRepository accountRepository;
	
	@Override
	public void onAuthenticationFailure(HttpServletRequest req, HttpServletResponse res, AuthenticationException auth)
			throws IOException, ServletException {
		
		String id = req.getParameter("username");
		
		Optional<Account> accountOp = accountRepository.findByIdAndDeleted(id, false);
		
		String returnVal = "/login?error=" + auth.getMessage();
		
		if(accountOp.isPresent()) {
			
			Integer count = accountOp.get().getFail();

			if(accountOp.get().getStatus() == 1) { // 사용가능일 경우에만
				if(count < 5) {  // 정상
					accountRepository.updateFail(id, count+1);
					returnVal = "/login?password="+(count+1);
				}else { //
					accountRepository.updateStatus(id, 2, count+1); // 비번잠금.
					returnVal = "/login?hold=";
				}
			}else if(accountOp.get().getStatus() == 2) {
				returnVal = "/login?hold=";
			}else if(accountOp.get().getStatus() == 3) {
				returnVal = "/login?stop=";
			}

		}
		
		res.sendRedirect(returnVal);

	}

	
}
