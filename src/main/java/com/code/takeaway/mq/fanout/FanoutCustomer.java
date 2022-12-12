package com.code.takeaway.mq.fanout;

import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author LiuQi
 * @version 1.0
 * @data 2022/12/9 15:48
 */
//@Component
@Slf4j
public class FanoutCustomer {

    @RabbitListener(bindings = {
            @QueueBinding(
                    value = @Queue, //创建临时队列
                    exchange = @Exchange(value = "logs", type = "fanout") //绑定的交换机
            )
    })
    public void receive1(String message) {
        log.info("fanout 广播 接收消息 一 {}", message);
    }

    @RabbitListener(bindings = {
            @QueueBinding(value = @Queue, //创建临时队列
                    exchange = @Exchange(value = "logs", type = "fanout")) //绑定的交换机
    })
    public void receive2(String message) {
        log.info("fanout 广播 接收消息 二 {}", message);
    }
}
