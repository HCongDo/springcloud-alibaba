package com.study.service.impl;

import com.study.service.BusinessService;
import org.springframework.stereotype.Component;

/**
 * @author hecong
 * @version 1.0
 * @date 2024/1/15 22:31
 */
@Component
public class BusinessServiceImpl implements BusinessService {
    @Override
    public void doSomething() {
        System.out.println("Invoke BusinessService's method : doSomething");
    }

    @Override
    public boolean checkSomething() {
        System.out.println("Invoke BusinessService's method : checkSomething");
        return true;
    }
}
