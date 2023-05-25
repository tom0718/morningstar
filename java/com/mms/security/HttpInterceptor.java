package com.mms.security;

import com.mms.model.SessionUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Component
@Slf4j
public class HttpInterceptor implements HandlerInterceptor {

    private SessionUser sessionUser;

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler) {

        String url = request.getRequestURI();
        HttpSession session = request.getSession();

        sessionUser = new SessionUser();
        sessionUser = (SessionUser) session.getAttribute("mms");

        log.info("url ====> {}", url);

        if(ObjectUtils.isEmpty(sessionUser)) {

            try { response.sendRedirect("/login"); }
            catch (IOException ignored) {}
            return false;
        } else {
			return true;
        }
    }


    @Override
    public void postHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            ModelAndView modelAndView) {

        //log.info("============== Interceptor > postHandle");
    }

    @Override
    public void afterCompletion(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            Exception ex) {

        //log.info("================ Interceptor > Completed");
    }


}
