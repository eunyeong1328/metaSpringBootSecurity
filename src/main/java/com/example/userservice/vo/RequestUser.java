package com.example.userservice.vo;

import lombok.Data;

@Data
public class RequestUser {
    private  String email;
    private String name;
    private String pwd; //나중에 암호화
}//회원가입 시 넘어오는 데이터 
//성격이 분명해서 나중에 수정되도 잘 변화되지 않는다.