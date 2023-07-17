package com.study.order.controller;

import com.study.common.entity.ResultDto;
import com.study.common.feign.ProductFeignService;
import com.study.common.feign.SeaFeignService;
import com.study.order.entity.Person;
import com.study.order.mapper.PersonMapper;
import io.seata.core.context.RootContext;
import io.seata.spring.annotation.GlobalTransactional;
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
    PersonMapper personMapper;

    @Autowired
    ProductFeignService productFeignService;

    @RequestMapping("test")
    @GlobalTransactional(rollbackFor = Exception.class)
    public ResultDto test(){
        Person person = new Person();
        person.setName("蜘蛛侠");
        person.setAge(99);
        int insert = personMapper.insert(person);
        logger.info("seata-全局事务ID: {}", RootContext.getXID());
        logger.info("seata-person插入结果: {}",insert);
        ResultDto seata = productFeignService.seata();
        logger.info("seata-FeignService插入结果: {}",seata.getData());
        int i = 1/0;
        return ResultDto.success("seata test success");
    }


}
