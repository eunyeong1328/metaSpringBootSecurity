package com.example.userservice.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration //설정파일역할
@EnableWebSecurity//필터링 작업
public class WebSecurity extends WebSecurityConfigurerAdapter { //security 관련한 가장 중요한 작업

    @Override//(사용자,관리자)권한관련 설정
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests().antMatchers("/users").permitAll();//user만 인증가능
        http.authorizeRequests().antMatchers("/h2-console/**").permitAll();

        http.headers().frameOptions().disable();
    }
}
