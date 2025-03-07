package com.su.yupao;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.su.yupao.mapper")
//在springboot中开启对定时任务的支持
@EnableScheduling
public class YuPaoApplication {
    public static void main(String[] args) {
        SpringApplication.run(YuPaoApplication.class, args);
    }

}
