package com.study.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.study.order.entity.Customer;
import org.apache.ibatis.annotations.Param;

//@Mapper   启动类用@MapperScan注解说明了扫描路径便可注释此注解
public interface CustomerMapper extends BaseMapper<Customer> {

    void updateCustomerAge(@Param("id") Integer id, @Param("age") Integer age);
}
