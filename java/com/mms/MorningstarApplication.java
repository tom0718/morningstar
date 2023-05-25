package com.mms;

import java.util.Collections;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.SessionCookieConfig;
import javax.servlet.SessionTrackingMode;
import javax.servlet.http.HttpSessionListener;

import com.mms.security.HttpInterceptor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.mms.security.CustomLoginSuccessFailHandler;
import com.mms.security.CustomLoginSuccessHandler;
import com.mms.security.SessionListener;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication
@EnableScheduling
public class MorningstarApplication extends WebMvcConfigurerAdapter {

	@Autowired
	HttpInterceptor httpInterceptor;

	public static void main(String[] args) {
		SpringApplication.run(MorningstarApplication.class, args);
	}
	
	@Bean
	public PasswordEncoder password(){
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public AuthenticationSuccessHandler successHandler() {
	    return new CustomLoginSuccessHandler("/account/loginProcess");
	}
	
	@Bean
	public AuthenticationFailureHandler successFailHandler() {
		return new CustomLoginSuccessFailHandler();
	}
	
	@Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
	
	@Bean
    public ServletContextInitializer servletContextInitializer() {
        return new ServletContextInitializer() {

            @Override
            public void onStartup(ServletContext servletContext) throws ServletException {
               servletContext.setSessionTrackingModes(Collections.singleton(SessionTrackingMode.COOKIE));
               SessionCookieConfig sessionCookieConfig=servletContext.getSessionCookieConfig();
               sessionCookieConfig.setHttpOnly(true);
            }
        };
	}

	@Bean
	  public HttpSessionListener httpSessionListener(){
	    return new SessionListener();
	 }

	@Bean
	public SessionRegistry sessionRegistry() {
		SessionRegistry sessionRegistry = new SessionRegistryImpl();
		return sessionRegistry;
	}


	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(httpInterceptor)
				.addPathPatterns("/account/**","/user/**","/dashboard","/word/**")
				.excludePathPatterns("/account/loginProcess");
	}
}
