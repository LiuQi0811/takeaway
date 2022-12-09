package com.code.takeaway.mq.topic;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author LiuQi
 * @version 1.0
 * @data 2022/12/9 16:44
 */
@Component
@Slf4j
public class TopicCustomer
{
    @RabbitListener(
            bindings = {
                    @QueueBinding(
                            value = @Queue, //创建临时队列
                            exchange = @Exchange(value = "topic",type = "topics"),// 动态路由  订阅模式
                            key = {"user.save","user.*"}
                    )
            }
    )
    public void receive1(String message){
        log.info("动态路由  订阅模式 接收 一 {}",message);
    }


    @RabbitListener(
            bindings = {
                    @QueueBinding(
                            value = @Queue,
                            exchange = @Exchange(value = "topic",type = "topics"), // 动态路由  订阅模式
                            key = {"order.#","product.#","user.*"}
                    )
            }
    )
    public void receive2(String message){
        log.info("动态路由  订阅模式 接收 二 {}",message);
    }
}
