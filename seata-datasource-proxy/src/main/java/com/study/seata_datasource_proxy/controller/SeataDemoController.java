package com.study.seata_datasource_proxy.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.study.common.entity.ResultDto;
import com.study.seata_datasource_proxy.entity.Customer;
import com.study.seata_datasource_proxy.mapper.CustomerMapper;
import io.seata.core.context.RootContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/seata")
public class SeataDemoController {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    CustomerMapper customerMapper;


    /**
     * seata 与 mybatis plus 集成存在以下冲突：
     * 1、BaseMapper 中的 insert 函数如果不指定主键，事务便不会回滚
     * 2、分页插件、自动填充等功能失效
     * 3、全局异常让事务失效
     * 4、feign 熔断降级处理使事务失效
     * @return
     */
    @RequestMapping("/test")
    public ResultDto test(){
        int pageNum = 1,pageSize=4;
        Page<Customer> page = new Page(pageNum, pageSize);
        QueryWrapper<Customer> queryWrapper = new QueryWrapper<Customer>();
        Page<Customer> customerPage = customerMapper.selectPage(page, queryWrapper);
        logger.info("seata 分页测试结果：{}",customerPage.getRecords().size());
        Customer customer = new Customer();
        customer.setName("王五");
        customer.setAge(99);
        customerMapper.updateCustomerAge();
//        int i = 1 / 0;
//        int insert = customerMapper.insert(customer);
//        logger.info("seata-customer插入结果: {}",insert);
        logger.info("seata-全局事务ID: {}", RootContext.getXID());
        return ResultDto.success("seata-b test success");
    }


}
