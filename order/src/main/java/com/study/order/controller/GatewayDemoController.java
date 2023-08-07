package com.study.order.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.study.common.entity.JwtInfo;
import com.study.common.entity.ResultDto;
import com.study.common.entity.UserContextHolder;
import com.study.order.entity.Customer;
import com.study.order.entity.Person;
import com.study.order.mapper.CustomerMapper;
import com.study.order.mapper.PersonMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Gateway 网关测试Demo
 *
 * @author hecong
 * @version 1.0
 * @date 2023/7/8 11:25
 */
@RestController
@RequestMapping("/gateway")
public class GatewayDemoController {

  Logger logger = LoggerFactory.getLogger(this.getClass());

  @Autowired
  PersonMapper personMapper;

  @Autowired
  CustomerMapper customerMapper;


  /**
   * 案例：gateway 延时熔断降级
   *
   * @return
   */
  @RequestMapping("/err")
  @ResponseBody
  public ResultDto err() throws Exception {
//        Thread.sleep(5000);
    int i = 1 / 0;
    List<Person> persons = personMapper.selectPersonList();
    return ResultDto.success(persons);
  }


  /**
   * 案例：gateway 查询列表
   *
   * @return
   */
  @RequestMapping("/list")
  @ResponseBody
  public ResultDto list() throws Exception {
    JwtInfo context = UserContextHolder.getInstance().getContext();
    logger.info("当前用户信息为：{}", context.toString());
    List<Person> persons = personMapper.selectPersonList();
    return ResultDto.success(persons);
  }


  /**
   * 案例：mybatis-plus  分页插件
   *
   * @return
   */
  @RequestMapping("/page")
  @ResponseBody
  public ResultDto page() throws Exception {
    int pageNum = 1, pageSize = 4;
    Page<Customer> page = new Page(pageNum, pageSize);
    QueryWrapper<Customer> queryWrapper = new QueryWrapper<Customer>();
    Page<Customer> customerPage = customerMapper.selectPage(page, queryWrapper);
    return ResultDto.success(customerPage);
  }


}
