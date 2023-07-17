package com.study;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author hecong
 * @version 1.0
 * @date 2023/7/8 11:13
 */
@SpringBootApplication
@EnableFeignClients
@MapperScan("com.study.order.mapper")
public class SeataA {


    public static void main(String[] args) {
        SpringApplication.run(SeataA.class,args);
    }


}
