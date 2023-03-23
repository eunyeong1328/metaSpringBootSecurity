package com.example.userservice.config;

import com.example.userservice.filter.MyFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class FilterConfig {
    @Autowired
    private Environment env;
    @Bean
    public FilterRegistrationBean<MyFilter> filter1(){
        FilterRegistrationBean<MyFilter> bean =
                new FilterRegistrationBean<>(new MyFilter(env));

        //URL 비교
        bean.addUrlPatterns("/user2/*");
        //bean .addUrlPatterns("/hello/*"); //주석 처리되도 잘 처리 됨
        bean.setOrder(0);

        return bean;
    }

}
