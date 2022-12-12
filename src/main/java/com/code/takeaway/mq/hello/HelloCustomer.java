package com.code.takeaway.mq.hello;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author LiuQi
 * @version 1.0
 * @data 2022/12/9 15:16
 */
//@Component
@RabbitListener(queuesToDeclare = @Queue(value = "hello"))
@Slf4j
public class HelloCustomer {
    @RabbitHandler
    public void receive(String message) {
        log.info(" 接收到的消息 {}" + message);
    }

}
