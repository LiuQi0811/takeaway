package com.code.takeaway.mq;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author LiuQi
 * @version 1.0
 * @data 2022/12/8 13:06
 * 第一种hello world模型使用 开发生产者
 */
@Component
public class HelloWorldProducer {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void  send(){
        rabbitTemplate.convertAndSend("sendMessage","hello my friend");
    }
}
