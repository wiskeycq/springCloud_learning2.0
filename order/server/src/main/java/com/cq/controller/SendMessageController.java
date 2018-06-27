package com.cq.controller;

import com.cq.DTO.OrderDTO;
import com.cq.message.stream.StreamClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @Auther: caoqsq
 * @Date: 2018/6/21 10:34
 * @Description: 消息发送端
 */
@RestController
public class SendMessageController {

    @Autowired
    private StreamClient streamClient;

/*    @GetMapping("/stream/send")
    public void process() {
        String message = "now"+new Date();
        streamClient.output().send(MessageBuilder.withPayload(message).build());
    }*/

    @GetMapping("/stream/sendObject")
    public void processObject() {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setOrderId("12345");
        streamClient.output().send(MessageBuilder.withPayload(orderDTO).build());
    }
}
