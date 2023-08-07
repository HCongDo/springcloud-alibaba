package com.study.product.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.study.common.entity.JwtInfo;
import com.study.common.entity.ResultDto;
import com.study.common.entity.UserContextHolder;
import com.study.common.utils.TraceIdUtil;
import com.study.product.entity.Customer;
import com.study.product.mapper.CustomerMapper;
import io.seata.core.context.RootContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/***
 * @Author 徐庶   QQ:1092002729
 * @Slogan 致敬大师，致敬未来的你
 */
@RestController
@RequestMapping("/product")
public class ProductController {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    CustomerMapper customerMapper;


    @RequestMapping("/seata")
    public ResultDto seata()  {
        JwtInfo context = UserContextHolder.getInstance().getContext();
        logger.info("产品用户信息为：{}",context.toString());
        int pageNum = 1,pageSize=4;
        Page<Customer> page = new Page(pageNum, pageSize);
        QueryWrapper<Customer> queryWrapper = new QueryWrapper<Customer>();
        Page<Customer> customerPage = customerMapper.selectPage(page, queryWrapper);
        logger.info("product 分页测试结果：{}",customerPage.getRecords().size());
        Customer customer = new Customer();
        customer.setName("王五");
        customer.setAge(99);
        customerMapper.updateCustomerAge();
//        int i = 1 / 0;
//        int insert = customerMapper.insert(customer);
//        logger.info("seata-customer插入结果: {}",insert);
        logger.info("product-全局事务ID: {}", RootContext.getXID());
        return ResultDto.success("seata-b test success");

    }


    @RequestMapping("/list")
    public ResultDto list()  {
        logger.info("traceId:{}", TraceIdUtil.getTraceId());
        JwtInfo context = UserContextHolder.getInstance().getContext();
        logger.info("产品用户信息为：{}",context.toString());
        int pageNum = 1,pageSize=4;
        Page<Customer> page = new Page(pageNum, pageSize);
        QueryWrapper<Customer> queryWrapper = new QueryWrapper<Customer>();
        Page<Customer> customerPage = customerMapper.selectPage(page, queryWrapper);
        logger.info("product 分页测试结果：{}",customerPage.getRecords().size());
        logger.info("product-全局事务ID: {}", RootContext.getXID());
        return ResultDto.success(customerPage);

    }



    @RequestMapping("/err")
    public ResultDto err()  {
        int i = 1 / 0;
        return ResultDto.success("err");

    }


}
