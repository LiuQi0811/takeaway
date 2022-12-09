package com.code.takeaway.mq.route;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author LiuQi
 * @version 1.0
 * @data 2022/12/9 16:26
 */
@Slf4j
@Component
public class RouteCustomer {
    @RabbitListener(
            bindings = {
            @QueueBinding(
                    value = @Queue, //创建临时队列
                    exchange = @Exchange(value = "directs", type = "direct"),//自定义交换机名称和类型
                    key = {"info", "error", "warn"}
            )
    })
    public void receive1(String message) {
        log.info("路由信息 一 {}", message);
    }

    @RabbitListener(
            bindings = {
                    @QueueBinding(
                            value = @Queue, //创建临时队列
                            exchange = @Exchange(value = "directs", type = "direct"),//自定义交换机名称和类型
                            key = {"error"}
                    )
            }
    )
    public void receive2(String message) {
        log.info("路由信息 二 {}", message);
    }
}
