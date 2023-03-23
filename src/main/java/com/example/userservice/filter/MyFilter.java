package com.example.userservice.filter;

import io.jsonwebtoken.Jwts;
import org.springframework.core.env.Environment;


import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import java.util.logging.LogRecord;


public class MyFilter implements Filter {
    private Environment env;

    public MyFilter(Environment env){
        this.env = env;
    }

    @Override
    public void doFilter(ServletRequest request2, ServletResponse response2, FilterChain chain) throws IOException, ServletException {
        System.out.println("필터 호출");
        HttpServletRequest request = (HttpServletRequest) request2;
        HttpServletResponse response = (HttpServletResponse)  response2; //우리가 원하는 객체

        if(request.getHeader("AUTHORIZATION") == null){ //권한이 없으면
            onError(response, "UNAUTHORIZATION");
        }else{ //권한이 있으면
            String authorizationHeader  = request.getHeader("AUTHORIZATION");
            System.out.println(authorizationHeader);
            String jwt = authorizationHeader.replace("Bearer",""); //Bearer값을 지우기

            if(!isJwtValid(jwt)){
                onError(response, "UNAUTHORIZATION");
            }
        }

        chain.doFilter(request2, response2); //chain이 끊어지지 않고 계속 사용 가능(로그인 후 계속 사용 가능)
    }

    private boolean isJwtValid(String jwt) {
        boolean returnValue = true;

        String subject = null;
        try{
            subject = Jwts.parser().setSigningKey(
                    env.getProperty("token.secret"))
                        .parseClaimsJws(jwt)
                        .getBody()
                        .getSubject();
        }catch (Exception e){
            returnValue = false;
        }

        if(subject == null || subject.isEmpty()){
            returnValue = false;
        }

        return returnValue;
    }

    private void onError(HttpServletResponse response, String httpStatus)
            throws IOException{
        response.addHeader("error",httpStatus);
        response.sendError(HttpServletResponse.SC_FORBIDDEN, httpStatus);
    }
}
