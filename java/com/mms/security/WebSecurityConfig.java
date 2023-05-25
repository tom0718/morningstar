package com.mms.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserDetailsService userDetailsService;
    
    @Autowired
    AuthenticationSuccessHandler successHandler;
    
    @Autowired
    AuthenticationFailureHandler failureHandler;



    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }
    
    @Override
	public void configure(WebSecurity web) throws Exception{
		web.ignoring().antMatchers("/fonts/**","/css/**","/scss/**", "/js/**", "/img/**", "/repository/**");
	}

    @Override      
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.headers().frameOptions().disable();
        http.headers().cacheControl().disable();
        http.authorizeRequests()
                .antMatchers("/account/loginProcess").authenticated()
                .antMatchers("/account/profile").authenticated()
                .antMatchers("/account/modifyProfile").authenticated()
                .antMatchers("/account/checkEmail").authenticated()
                .antMatchers("/enrollAttend").authenticated()
                .antMatchers("/dashboard").authenticated()
                .antMatchers("/account/**").hasAnyRole("SUPREME","ADMIN")
                .antMatchers("/user/**").hasAnyRole("SUPREME","STAFF","PASTOR")
                .antMatchers("/userList","/userView/**").hasAnyRole("SUPREME","MANAGER")
                .antMatchers("/word/**").hasAnyRole("SUPREME","WORD","PASTOR")
                .antMatchers("/login").permitAll()
                .antMatchers("/check").permitAll()
                .antMatchers("/checkMember").permitAll()
                .antMatchers("/checkView").permitAll()
                .antMatchers("/changeProfileImage").permitAll()
                .antMatchers("/logout").permitAll()
                .antMatchers("/error").permitAll()
                .antMatchers("/room/**").permitAll()
                .antMatchers("/**").permitAll()
                .anyRequest().permitAll();

        http.formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .failureUrl("/login?error")
                .usernameParameter("username")
                .passwordParameter("password")
                .successHandler(successHandler)
                .failureHandler(failureHandler);

        http.logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login")
                .invalidateHttpSession(true)
                .and()
                .sessionManagement()
                .maximumSessions(1)
                .expiredUrl("/login?expired");
	            
	        
	            
        
        http.exceptionHandling().accessDeniedPage("/login?auth=");
        http.sessionManagement().invalidSessionUrl("/login");
        

    }



}

