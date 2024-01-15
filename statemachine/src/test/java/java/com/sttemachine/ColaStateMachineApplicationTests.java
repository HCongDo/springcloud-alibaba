package java.com.sttemachine;

import com.alibaba.cola.statemachine.StateMachine;
import com.alibaba.cola.statemachine.StateMachineFactory;
import com.study.service.BusinessService;
import com.study.statemachine.StateMachineContext;
import com.study.statemachine.StateMachineInit;
import com.study.statemachine.enums.GigaEvents;
import com.study.statemachine.enums.GigaStates;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.UUID;
import javax.annotation.Resource;
/**
 * @author hecong
 * @version 1.0
 * @date 2024/1/15 22:43
 */
@SpringBootTest
class ColaStateMachineApplicationTests {

    @Resource
    private BusinessService businessService;

    @Test
    void testStateMachine() {
        // get state machine instance
        StateMachine<GigaStates,GigaEvents,StateMachineContext> stateMachine =  StateMachineFactory.get(StateMachineInit.MACHINE_ID);

        // fire event test
        GigaStates target = stateMachine.fireEvent(GigaStates.STATE1, GigaEvents.EVENT1, new StateMachineContext("Giga", UUID.randomUUID().toString(), businessService));
        Assertions.assertEquals(GigaStates.STATE2, target);
        target = stateMachine.fireEvent(GigaStates.STATE2, GigaEvents.INTERNAL_EVENT, new StateMachineContext("Giga", UUID.randomUUID().toString(), businessService));
        Assertions.assertEquals(GigaStates.STATE2, target);
        target = stateMachine.fireEvent(GigaStates.STATE2, GigaEvents.EVENT2, new StateMachineContext("Giga", UUID.randomUUID().toString(), businessService));
        Assertions.assertEquals(GigaStates.STATE1, target);
        target = stateMachine.fireEvent(GigaStates.STATE1, GigaEvents.EVENT3, new StateMachineContext("Giga", UUID.randomUUID().toString(), businessService));
        Assertions.assertEquals(GigaStates.STATE3, target);

        //---- Print plantUML ----//
        System.out.println(stateMachine.generatePlantUML());
        //-----------------------//
    }


}
