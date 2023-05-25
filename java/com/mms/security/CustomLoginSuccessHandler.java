package com.mms.security;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.mms.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

public class CustomLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Autowired
    private AccountRepository accountRepository;

    public CustomLoginSuccessHandler(String defaultTargetUrl) {
        setDefaultTargetUrl(defaultTargetUrl);
    }

    @Override
    public void onAuthenticationSuccess(
    		HttpServletRequest request, 
    		HttpServletResponse response, 
    		Authentication authentication) throws ServletException, IOException {
       
        HttpSession session = request.getSession();

        String id = request.getParameter("username");

        accountRepository.updateLoginDate(id, Date.from(LocalDateTime.now().atZone(ZoneId.of("Asia/Seoul")).toInstant()));

        if (session != null) {
            getRedirectStrategy().sendRedirect(request, response, "/account/loginProcess");
        } else {
            super.onAuthenticationSuccess(request, response, authentication); //중간 단계가 없으면 이것만 있으면 됨.
        }

    }
}
