package com.code.takeaway.mq.work;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/*
 *@ClassName WorkQueueCustomer
 *@Description TODO
 *@Author LiuQi
 *@Date 2022/12/10 21:43
 *@Version 1.0
 */
@Slf4j
public class WorkQueueCustomer {
    @RabbitListener(queues = "work.queue")
    public void receive1(String message)throws Exception{

        log.info(" 壹号 接收信息  {} 时间：{}",message, LocalDateTime.now());
        Thread.sleep(20);
    }

    @RabbitListener(queues = "work.queue")
    public void receive2(String message) throws InterruptedException {
        log.error(" 贰号 接收信息  {} 时间：{}",message, LocalDateTime.now());
        Thread.sleep(200);
    }
}
