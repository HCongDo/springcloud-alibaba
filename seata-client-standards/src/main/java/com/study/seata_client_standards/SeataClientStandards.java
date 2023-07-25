package com.study.seata_client_standards;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * seat 客户端标准模式 - 自动代理数据源
 * @author hecong
 * @version 1.0
 * @date 2023/7/8 11:13
 */
@SpringBootApplication
@EnableFeignClients
@MapperScan("com.study.seata_client_standards.mapper")
public class SeataClientStandards {


    public static void main(String[] args) {
        SpringApplication.run(SeataClientStandards.class,args);
    }


}
