package com.study.rocketmq;

import com.alibaba.fastjson.JSON;
import com.study.common.entity.JwtInfo;
import com.study.common.entity.UserContextHolder;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * rocketmq 生产者实例
 *
 * @author Administrator
 * @version 1.0
 * @date 2023/8/12 18:02
 */
@RestController
@RequestMapping("/rockermq/product")
public class RocketMqProductDemoEndpoint {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${rocketmq.producer.send-message-timeout}")
    private Integer messageTimeOut;

    // 建议正常规模项目统一用一个TOPIC
    private static final String topic = "RLT_TEST_TOPIC";

    // 直接注入使用，用于发送消息到broker服务器
    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    /**
     * 普通发送（这里的参数对象User可以随意定义，可以发送个对象，也可以是字符串等）
     */
    @RequestMapping("/send")
    public void send() {
        logger.info("Rocket-普通发送");
        JwtInfo context = UserContextHolder.getInstance().getContext();
        rocketMQTemplate.convertAndSend(topic + ":tag1", context);
    }

    /**
     * 发送同步消息（阻塞当前线程，等待broker响应发送结果，这样不太容易丢失消息）
     * （msgBody也可以是对象，sendResult为返回的发送结果）
     */
    @RequestMapping("/sendMsg")
    public SendResult sendMsg() {
        logger.info("Rocket-发送同步消息");
        JwtInfo context = UserContextHolder.getInstance().getContext();
        SendResult sendResult = rocketMQTemplate.syncSend(topic, MessageBuilder.withPayload(context).build());
        logger.info("【sendMsg】sendResult={}", JSON.toJSONString(sendResult));
        return sendResult;
    }


    /**
     * 发送异步消息（通过线程池执行发送到broker的消息任务，执行完后回调：在SendCallback中可处理相关成功失败时的逻辑）
     * （适合对响应时间敏感的业务场景）
     */
    @RequestMapping("/sendAsyncMsg")
    public void sendAsyncMsg() {
        logger.info("Rocket-发送异步消息");
        JwtInfo context = UserContextHolder.getInstance().getContext();
        rocketMQTemplate.asyncSend(topic, MessageBuilder.withPayload(context).build(), new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                // 处理消息发送成功逻辑
                logger.info("处理消息发送成功逻辑");
            }
            @Override
            public void onException(Throwable throwable) {
                // 处理消息发送异常逻辑
                logger.error("处理消息发送异常逻辑");
            }
        });
    }


    /**
     * 发送延时消息（上面的发送同步消息，delayLevel的值就为0，因为不延时）
     * 在start版本中 延时消息一共分为18个等级分别为：1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h
     */
    @RequestMapping("/sendDelayMsg")
    public void sendDelayMsg() {
        logger.info("Rocket-发送延时消息");
        JwtInfo context = UserContextHolder.getInstance().getContext();
        rocketMQTemplate.syncSend(topic, MessageBuilder.withPayload(context).build(), messageTimeOut, 2);
    }

    /**
     * 发送单向消息（只负责发送消息，不等待应答，不关心发送结果，如日志）
     */
    @RequestMapping("/sendOneWayMsg")
    public void sendOneWayMsg() {
        logger.info("Rocket-发送单向消息");
        JwtInfo context = UserContextHolder.getInstance().getContext();
        rocketMQTemplate.sendOneWay(topic, MessageBuilder.withPayload(context).build());
    }


    /**
     * 顺序发送
     */
    @RequestMapping("/syncSendOrderly")
    public void syncSendOrderly() {
        logger.info("Rocket-顺序发送");
        JwtInfo context = UserContextHolder.getInstance().getContext();
        //参数一：topic   如果想添加tag,可以使用"topic:tag"的写法
        //参数二：消息内容
        //参数三：hashKey 用来计算决定消息发送到哪个消息队列， 一般是订单ID，产品ID等
        String orderId = "123456";
        rocketMQTemplate.syncSendOrderly(topic, MessageBuilder.withPayload(context).build(),orderId);
    }



    /**
     * 发送带tag的消息，直接在topic后面加上":tag"
     */
    @RequestMapping("/sendTagMsg")
    public SendResult sendTagMsg() {
        logger.info("Rocket-发送带tag的消息");
        JwtInfo context = UserContextHolder.getInstance().getContext();
        return rocketMQTemplate.syncSend(topic + ":tag2", MessageBuilder.withPayload(context).build());
    }

}
