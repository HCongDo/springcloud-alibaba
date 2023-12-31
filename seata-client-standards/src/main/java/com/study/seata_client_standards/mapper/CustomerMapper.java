package com.study.seata_client_standards.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.study.seata_client_standards.entity.Customer;

//@Mapper   启动类用@MapperScan注解说明了扫描路径便可注释此注解
public interface CustomerMapper extends BaseMapper<Customer> {

    void updateCustomerAge();

}