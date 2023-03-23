package com.example.userservice.vo;

import lombok.Data;

@Data//로그인 데이터
public class RequestLogin {
    private String email;
    private String password;
}
