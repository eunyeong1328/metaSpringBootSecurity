package com.example.userservice.jpa;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "users2")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //sql 값 자동 증가
    private Long id;

    @Column(nullable = false, unique = true) //null 허용 안함
    private String userId;
    @Column(nullable = false, unique = true) //null 허용 안함
    private String email;
    @Column(nullable = false, unique = true, length = 50)
    private String name;
    @Column(nullable = false, unique = true) //null 허용 안함
    private String encrypedPwd;
}//도메인 객체
