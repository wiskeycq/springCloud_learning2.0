package com.cq.message;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @Auther: caoqsq
 * @Date: 2018/6/25 15:13
 * @Description: 接受mq消息（消费者）
 */
@Component
@Slf4j
public class MqReceiver {

    //@RabbitListener(queues = "myQueue") //1.这种需要在rabitmq管理端加上myQueue这个队列
    //@RabbitListener(queuesToDeclare = @Queue("myQueue")) //2.这种可以自动创建队列
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue("myQueue"),exchange = @Exchange("myExchange"))) //3.自动创建，Queue和Exchange绑定
    public void process(String message) {
        log.info("MqReceiver:{}",message);
    }

    /**
     * 数码供应商服务，接受消息
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue("computerQueue"),
            exchange = @Exchange("OrderExchange"),
            key = "computer")) //3.自动创建，Queue和Exchange绑定
    public void processComputer(String message) {
        log.info("computer MqReceiver:{}",message);
    }

    /**
     * 水果供应商服务，接受消息
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue("fruitQueue"),
            exchange = @Exchange("OrderExchange"),
            key = "fruit")) //3.自动创建，Queue和Exchange绑定
    public void processFruit(String message) {
        log.info("fruit MqReceiver:{}",message);
    }
}
