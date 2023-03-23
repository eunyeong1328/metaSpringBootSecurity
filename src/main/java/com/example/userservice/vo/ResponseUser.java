package com.example.userservice.vo;

import lombok.Data;

@Data
public class ResponseUser {
    private String email;
    private String name;
    private  String userId;
}//클라이언트가 보고 싶은 데이터만 보내주는 것
