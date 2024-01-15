package com.study.controller;

import com.alibaba.cola.statemachine.StateMachine;
import com.alibaba.cola.statemachine.StateMachineFactory;
import com.study.service.BusinessService;
import com.study.statemachine.StateMachineContext;
import com.study.statemachine.StateMachineInit;
import com.study.statemachine.enums.GigaEvents;
import com.study.statemachine.enums.GigaStates;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * @author hecong
 * @version 1.0
 * @date 2024/1/15 22:32
 */
@RestController
public class TestController {


    private final BusinessService businessService;

    public TestController(BusinessService businessService) {
        this.businessService = businessService;
    }

    @GetMapping("/test/ddd")
    public String testDDD() {
        StateMachine<GigaStates, GigaEvents, StateMachineContext> stateMachine = StateMachineFactory.get(StateMachineInit.MACHINE_ID);
        GigaStates target = stateMachine.fireEvent(GigaStates.STATE1, GigaEvents.EVENT1,
                new StateMachineContext("Giga", UUID.randomUUID().toString(), businessService));
        return target.name();
    }
}
