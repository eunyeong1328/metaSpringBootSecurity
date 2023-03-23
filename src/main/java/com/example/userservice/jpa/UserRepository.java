package com.example.userservice.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByUserId(String userId);
    UserEntity findByEmail(String username);//username은 email을 의미
}
