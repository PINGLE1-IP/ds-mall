package com.ds.mall;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.ds.mall.**.mapper")
@SpringBootApplication
public class DsMallApplication {

    public static void main(String[] args) {
        SpringApplication.run(DsMallApplication.class, args);
    }
}
