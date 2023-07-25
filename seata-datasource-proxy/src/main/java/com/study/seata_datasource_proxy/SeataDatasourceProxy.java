package com.study.seata_datasource_proxy;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * seata 客户端手动代理数据源模式
 * @author hecong
 * @version 1.0
 * @date 2023/7/8 11:13
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableFeignClients
@MapperScan("com.study.seata_datasource_proxy.mapper")
public class SeataDatasourceProxy {


    public static void main(String[] args) {
        SpringApplication.run(SeataDatasourceProxy.class,args);
    }


}
