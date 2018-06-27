package com.cq.message.stream;

import com.cq.DTO.OrderDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

/**
 * @Auther: caoqsq
 * @Date: 2018/6/25 16:47
 * @Description: //消息接受端
 */
@Component
@EnableBinding(value = StreamClient.class)
@Slf4j
public class StreamReceiver {

  /*  @StreamListener(value = StreamClient.INPUT)
    public void process(Object message) {
        log.info("收到的消息为:{}",message);
    }*/

   /* @StreamListener(value = StreamClient.INPUT)
    public void process(OrderDTO message) {
        log.info("收到的消息为:{}",message);
    }*/

/*    @StreamListener(value = StreamClient.INPUT)
    @SendTo(StreamClient.INPUT2)//消息接受到之后想回发送方一个消息，可以用这个注解
    public String process(OrderDTO message) {
        log.info("收到的消息为:{}",message);
        //发送mq消息
        return "received.";
    }

    //上面返回的内容通过消息发送到INPUT2
    @StreamListener(value = StreamClient.INPUT2)
    public void process2(String message) {
        log.info("回调收到的消息为:{}",message);
    }*/
}
