package com.study.workflow;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author hecong
 * @version 1.0
 * @date 2023/7/8 11:15
 */
@SpringBootApplication
@MapperScan("com.study.workflow.mapper")
public class ActivityFlowApplication {

    public static void main(String[] args) {
        SpringApplication.run(ActivityFlowApplication.class,args);
    }
}
