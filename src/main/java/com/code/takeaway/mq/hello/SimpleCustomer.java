package com.code.takeaway.mq.hello;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/*
 *@ClassName SimpleCustomer
 *@Description TODO
 *@Author LiuQi
 *@Date 2022/12/10 20:21
 *@Version 1.0
 */
@Slf4j
public class SimpleCustomer
{

    @RabbitListener(queues = "simple.queue")
    public void receive(String message){

        log.info("simple queue {}",message);

    }
}
