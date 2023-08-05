package com.study.stock.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.study.common.entity.JwtInfo;
import com.study.common.entity.ResultDto;
import com.study.common.entity.User;
import com.study.common.entity.UserContextHolder;
import com.study.stock.entity.Customer;
import com.study.stock.mapper.CustomerMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stock")
public class StockController {

    @Autowired
    private CustomerMapper customerMapper;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping("/reduct")
    @ResponseBody
    public ResultDto reduct() throws Exception{
        JwtInfo context = UserContextHolder.getInstance().getContext();
        logger.info("库存用户信息为：{}",context.toString());
        int pageNum = 1,pageSize=4;
        Page<Customer> page = new Page(pageNum, pageSize);
        QueryWrapper<Customer> queryWrapper = new QueryWrapper<Customer>();
        Page<Customer> customerPage = customerMapper.selectPage(page, queryWrapper);
        logger.info("stock 分页测试结果：{}",customerPage.getRecords().size());
        return ResultDto.success(page);
    }
}
