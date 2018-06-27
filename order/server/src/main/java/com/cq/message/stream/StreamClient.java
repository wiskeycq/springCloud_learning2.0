package com.cq.message.stream;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

/**
 * @Auther: caoqsq
 * @Date: 2018/6/25 16:44
 * @Description:
 */
public interface StreamClient {

    String INPUT ="myMessage5";
    String INPUT2 ="myMessage6";

/*    @Input(value = StreamClient.INPUT)
    SubscribableChannel input();*/


    @Output(value = StreamClient.INPUT)
    MessageChannel output();

    @Output(value = StreamClient.INPUT2)
    MessageChannel output2();

}
