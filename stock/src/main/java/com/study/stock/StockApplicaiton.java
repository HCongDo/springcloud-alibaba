package com.study.stock;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author hecong
 * @version 1.0
 * @date 2023/7/8 11:15
 */
@SpringBootApplication
@EnableFeignClients
@MapperScan("com.study.stock.mapper")
public class StockApplicaiton {

    public static void main(String[] args) {
        SpringApplication.run(StockApplicaiton.class,args);
    }
}
