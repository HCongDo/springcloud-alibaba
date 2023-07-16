package com.tulingxueyuan.product.controller;

import com.tulingxueyuan.product.entity.Customer;
import com.tulingxueyuan.product.mapper.CustomerMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
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
//    @Transactional
    public String seata()  {
        Customer customer = new Customer();
        customer.setName("王五");
        customer.setPhone("15700739495");
        customer.setAge(14);
        int insert = customerMapper.insert(customer);
        logger.info("product-customer插入结果: {}",insert);
        return "seata test result";
    }



}
