package com.code.takeaway.mq.work;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author LiuQi
 * @version 1.0
 * @data 2022/12/9 15:36
 */
@Component
@Slf4j
public class WorkCustomer {
    // 消费者一
    @RabbitListener(queuesToDeclare = @Queue("work"))
    public void receive1(String message) {
     log.info("接收消息 一 {}"+message );
    }
    // 消费者二
    @RabbitListener(queuesToDeclare = @Queue("work"))
    public void receive2(String message) {
        log.info("接收消息 二 {}"+message);
    }
}
