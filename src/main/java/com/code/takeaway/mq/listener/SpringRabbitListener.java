package com.code.takeaway.mq.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;

/*
 *@ClassName SpringRabbitListener
 *@Description TODO
 *@Author LiuQi
 *@Date 2022/12/10 22:55
 *@Version 1.0
 */
@Slf4j
@Component
public class SpringRabbitListener {

    @RabbitListener(queues = "simple.queue")
    public void listenerSimpleQueue(String message){

        log.info("simple queue {}",message);

    }

    @RabbitListener(queues = "work.queue")
    public void listenerWorkQueue1(String message)throws Exception{

        log.info(" 壹号 接收信息  {} 时间：{}",message, LocalDateTime.now());
        Thread.sleep(20);
    }

    @RabbitListener(queues = "work.queue")
    public void listenerWorkQueue2(String message) throws InterruptedException {
        log.info(" 贰号 接收信息  {} 时间：{}",message, LocalDateTime.now());
        Thread.sleep(200);
    }

    @RabbitListener(queues = "fanout.queue1")
    public void listenerFanoutQueue1(String message){
        log.info("消费者接收到fanout.queue1的消息 {}",message);
    }
    @RabbitListener(queues = "fanout.queue2")
    public void listenerFanoutQueue2(String message){
        log.info("消费者接收到fanout.queue2的消息 {}",message);
    }

    @RabbitListener(bindings = {
            @QueueBinding(value = @Queue(name = "direct.queue1"), //创建临时队列
                    exchange = @Exchange(value = "amqp.direct", // 创建交换机
                            type = ExchangeTypes.DIRECT), // 指定交换机类型
                    key = {"red","blue"}
            )

    })
    public void listenerDirectQueue1(String message){
        log.info("消费者接收到direct.queue1的消息 {}",message);
    }

    @RabbitListener(bindings = {
            @QueueBinding(value = @Queue(name = "direct.queue2"), //创建临时队列
                    exchange = @Exchange(value = "amqp.direct", // 创建交换机
                            type = ExchangeTypes.DIRECT), // 指定交换机类型
                    key = {"red","yellow"}
            )

    })
    public void listenerDirectQueue2(String message){
        log.info("消费者接收到direct.queue2的消息 {}",message);
    }



    @RabbitListener(bindings = {
            @QueueBinding(value = @Queue(name = "topic.queue1"),
                    exchange = @Exchange(value = "amqp.topic",type = ExchangeTypes.TOPIC),
                    key = "china.#" )

    })
    public void listenerTopicQueue1(String message)
    {
        log.info("消费者接收到topic.queue1的消息 {}",message);
    }    @RabbitListener(bindings = {
            @QueueBinding(value = @Queue(name = "topic.queue2"),
                    exchange = @Exchange(value = "amqp.topic",type = ExchangeTypes.TOPIC),
                    key = "#.news" )

    })
    public void listenerTopicQueue2(String message)
    {
        log.info("消费者接收到topic.queue2的消息 {}",message);
    }


    /**
     *  Map 类型消息
     * * @param message
     */
//    @RabbitListener(queues = "object.queue")
    public void listenerObjectQueue(HashMap<String, Object> objectObjectHashMap){
        log.info("消费者接收到object.queue的消息 {}",objectObjectHashMap);
    }


}
