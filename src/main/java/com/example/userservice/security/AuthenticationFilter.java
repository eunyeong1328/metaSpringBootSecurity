package com.example.userservice.security;

import com.example.userservice.dto.UserDto;
import com.example.userservice.service.UserService;
import com.example.userservice.vo.RequestLogin;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter { //인증과 관련된 내용
    private UserService userService;
    private Environment env;
    //토큰 설정파일은 - yml파일
    public AuthenticationFilter(AuthenticationManager authenticationManager,
                                UserService userService, Environment env){
        super.setAuthenticationManager(authenticationManager);
        this.userService = userService;
        this.env = env;
    }
//Override는 framework 의 역할을 이 작업을 해주세요
    @Override //로그인 전 제일 먼저 호출(토큰을 만들어서 보냄, 로그인   인증되기 전 준비 작업) , 인증하기 전  호출
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try{
            RequestLogin creds  = new ObjectMapper().readValue(request.getInputStream(), //입력스트림으로 우리가 입력한 id, pw를 가져온다.
                    RequestLogin.class);

            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken( //입력도니 id, pw를 token형태로 바꿈
                            creds.getEmail(),
                            creds.getPassword(),
                            new ArrayList<>()
                    )
            );
        }catch(IOException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        String userName = ((User) authResult.getPrincipal()).getUsername();
        //System.out.println("userName: " + userName);
        UserDto userDetail = userService.getUserDetailByEmail(userName);

        String token = Jwts.builder()
                .setSubject(userDetail.getUserId()) //★☆★☆★☆
                .setExpiration(new Date(System.currentTimeMillis() +
                        Long.parseLong(env.getProperty("token.expiration_time"))))
                .signWith(SignatureAlgorithm.HS512, env.getProperty("token.secret"))
                .compact();

        response.addHeader("token", token);
        response.addHeader("userId", userDetail.getUserId());
    }

//    @Override //인증이 완료한 후 호출
//    protected void successfulAuthentication(HttpServletRequest request,
//                                            HttpServletResponse response,
//                                            FilterChain chain,
//                                            Authentication authResult) throws IOException, ServletException {
//        String userName = ((User) authResult.getPrincipal()).getUsername();
//        //System.out.println("userName : " + userName);
//
//        UserDto userDetail = userService.getUserDetailByEmail(userName); //detail의 정보를 가지고 토큰 생성
//
//        String token = Jwts.builder()
//                .setSubject(userDetail.getUserId())
//                .setExpiration(new Date(System.currentTimeMillis() +
//                        Long.parseLong(env.getProperty("token.expiration_time"))))//현재 시간을 더해줌
//                .signWith(SignatureAlgorithm.HS512, env.getProperty("token.secret"))
//                .compact();
//
//        response.addHeader("token", token);
//        response.addHeader("userId", userDetail.getUserId()); //두개의 값을 전달해서 비교한다.
////여기까지 클라이언트에 전달됨
//    }
}
