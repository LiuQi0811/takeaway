package com.code.takeaway.mq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author LiuQi
 * @version 1.0
 * @data 2022/12/8 13:07
 * 第一种hello world模型使用 开发消费者
 */
@Component
@RabbitListener(queuesToDeclare = @Queue("hello"))
@Slf4j
public class HelloWorldConsumer
{
    @RabbitHandler
    public void  receive(String message){
        log.info("接收到的消息 {}",message);
    }
}
