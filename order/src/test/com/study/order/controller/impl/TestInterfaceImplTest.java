package com.study.order.controller.impl;

import org.junit.Test;


/**
 * @author hecong
 * @version 1.0
 * @date 2023/9/26 12:00
 */
public class TestInterfaceImplTest {

    @Test
    public String a(){
        TestInterfaceImpl testInterface = new  TestInterfaceImpl();
        String a = testInterface.a();
        return a;
    }

}