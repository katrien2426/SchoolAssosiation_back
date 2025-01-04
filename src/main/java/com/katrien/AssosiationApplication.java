package com.katrien;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.katrien.mapper")
@EnableScheduling
public class AssosiationApplication {
    public static void main(String[] args) {
        SpringApplication.run(AssosiationApplication.class, args);
    }
}
