package com.cq;

import org.junit.Test;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @Auther: caoqsq
 * @Date: 2018/6/25 15:17
 * @Description: 发送mq消息测试
 */
@Component
public class MqSenderTest extends OrderApplicationTests {

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Test
    public void send() {
        amqpTemplate.convertAndSend("myQueue","当前时间为："+new Date());
    }

    @Test
    public void sendComputer() {
        amqpTemplate.convertAndSend("OrderExchange","computer","当前时间为："+new Date());
    }

    @Test
    public void sendFruit() {
        amqpTemplate.convertAndSend("OrderExchange","fruit","当前时间为："+new Date());
    }
}
