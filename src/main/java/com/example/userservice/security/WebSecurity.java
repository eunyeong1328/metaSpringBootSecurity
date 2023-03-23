package com.example.userservice.security;

import com.example.userservice.service.UserService;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.core.env.Environment;

@Configuration // spring-security 모은 인증 처리 / 설정 파일 역할
@EnableWebSecurity//필터링 작업
                                        //상속
public class WebSecurity extends WebSecurityConfigurerAdapter { //security 관련한 가장 중요한 작업
    private UserService userService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private Environment env;
    public WebSecurity(Environment env, UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder){
        this.env = env;
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override//(사용자,관리자)"권한"관련 설정 ,모든 권한 안됨
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests().antMatchers("/users").permitAll();  //필요한 권한(users)만 풀기
        http.authorizeRequests().antMatchers("/h2-console/**").permitAll();

        http.authorizeRequests().antMatchers("/**")//나머지 요청은
                .hasIpAddress("localhost")//locahost로 찍히는
                .and()
                .addFilter(getAuthenticationFiler()); //인증하기 위한 준비를 한다.
        //getAuthenticationFiler 사전에 해야할 작업 filter로 등록

        http.headers().frameOptions().disable();
    }
    private AuthenticationFilter getAuthenticationFiler() throws Exception {//필터 적용
       AuthenticationFilter authenticationFilter
               = new AuthenticationFilter(authenticationManager(),userService,env); //getAuthenticationFilerfmf 매니저가 관리
       return authenticationFilter;
    }

    @Override //"인증"과 관련된 내용,인증하는 역할
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {//넘어온 데이터값을
        auth.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder);//encoding 된 pw
        //우리가 만든 detail은
    }
}
