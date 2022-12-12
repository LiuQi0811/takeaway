package com.code.takeaway.mq.config;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*
 *@ClassName FanoutConfig
 *@Description TODO
 *@Author LiuQi
 *@Date 2022/12/10 22:17
 *@Version 1.0
 */
@Configuration
@Slf4j
public class FanoutConfig
{
    @Bean
    public FanoutExchange fanoutExchange(){
        log.info(" fanoutExchange  配置开启......   ");
         String exchangeName = "amqp.fanout"; // 交换机名称
        return new FanoutExchange(exchangeName);
    }

    @Bean
    public Queue fanoutQueue1(){ // 队列1
        log.info(" fanoutQueue1  配置开启......   ");
        String queueName ="fanout.queue1"; // 队列名称
        return new Queue(queueName);

    }

    @Bean
    public Queue fanoutQueue2(){ // 队列2
        log.info(" fanoutQueue2  配置开启......   ");
        String queueName ="fanout.queue2"; // 队列名称
        return new Queue(queueName);
    }

    @Bean
    public Binding fanoutBinding1(Queue fanoutQueue1,FanoutExchange fanoutExchange)
    { // 绑定队列1 到交换机
        return BindingBuilder.bind(fanoutQueue1).to(fanoutExchange);
    }

    @Bean
    public Binding fanoutBinding2(Queue fanoutQueue2,FanoutExchange fanoutExchange)
    { // 绑定队列2 到交换机
        return BindingBuilder.bind(fanoutQueue2).to(fanoutExchange);
    }




    @Bean
    public Queue objectQueue(){
        return new Queue("object.queue");
    }

}
