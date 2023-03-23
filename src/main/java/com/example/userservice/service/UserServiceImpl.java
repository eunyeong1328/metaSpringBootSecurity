package com.example.userservice.service;

import com.example.userservice.dto.UserDto;
import com.example.userservice.jpa.UserEntity;
import com.example.userservice.jpa.UserRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.UUID;

@Service//객체 생성
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override//★☆★☆★ id와 pw를 처리하는 부분 (내가 인증을 이렇게 하겠다 (자체적으로 정함))
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(username); 
        if(userEntity == null){
            throw new UsernameNotFoundException(username + ": not found"); //인증 안됨
        }                                                                                                   //true가 4개 가 들어감(걍 조건)
        return  new User(userEntity.getEmail(), userEntity.getEncrypedPwd(), true, true, true, true, new ArrayList<>()); //User가 UserDetail을 상속하고 있기 때문에 형변환시 타입이 같다
    } //User객체를 무조건 return 하기로 되어있다. /자젳적으로 인증이 되는지 않되는지 알아서 비교 = 추상화작업
//실질적인 인증 처리
    @Override
    public UserDto createUser(UserDto userDto) { //UserDto로 받아오길 원한다.
        userDto.setUserId(UUID.randomUUID().toString());//UUID 랜덤 생성
        //매핑 전략 설정
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        System.out.println("getData확인 : " + userDto.getPwd());
        UserEntity userEntity = mapper.map(userDto, UserEntity.class);
        userEntity.setEncrypedPwd(passwordEncoder.encode(userDto.getPwd())); //pw 암호화해서 pw들어감

        userRepository.save(userEntity); //db저장

        UserDto returnUserDto = mapper.map(userEntity, UserDto.class);//UserDto로 변환
        return returnUserDto;
    }
    @Override
    public UserDto getUserByUserId(String userId) {
        return null;
    }

    @Override
    public Iterable<UserEntity> getUserByAll() {
        return null;
    }
}
