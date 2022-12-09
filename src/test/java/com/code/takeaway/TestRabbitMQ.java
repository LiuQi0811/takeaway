package com.code.takeaway;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author LiuQi
 * @version 1.0
 * @data 2022/12/8 14:14
 */
@SpringBootTest(classes = TakeawayApplication.class)
@Slf4j
@RunWith(SpringRunner.class)
public class TestRabbitMQ {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    // hello world
    @Test
    public void testHello(){
        rabbitTemplate.convertAndSend("hello","hello world");
    }
    // work
    @Test
    public void testWork(){
        for (int i = 1; i < 8; i++) {
            rabbitTemplate.convertAndSend("work","work模型"+i);
        }
    }
    // fanout 广播
    @Test
    public void testFanout(){
        rabbitTemplate.convertAndSend("logs","","Fanout的模型发送的消息");
    }
    //route 路由模式
    @Test
    public void testRoute(){
        rabbitTemplate.convertAndSend("directs","error","发送info的key的路由信息");
    }
    //topic 动态路由  订阅模式
    @Test
    public void testTopic(){
        rabbitTemplate.convertAndSend("topics","product.save.add","produce.save.add 动态路由消息");
    }
}
