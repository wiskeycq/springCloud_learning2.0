package com.cq.message;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @Auther: caoqsq
 * @Date: 2018/6/25 16:28
 * @Description: mq发送端（生产者）
 */
@Component
public class MqSender {

    @Autowired
    private AmqpTemplate amqpTemplate;

    public void send() {
        amqpTemplate.convertAndSend("myQueue","当前时间为："+new Date());
    }

    public void sendComputer() {
        amqpTemplate.convertAndSend("OrderExchange","computer","当前时间为："+new Date());
    }

    public void sendFruit() {
        amqpTemplate.convertAndSend("OrderExchange","fruit","当前时间为："+new Date());
    }
}
