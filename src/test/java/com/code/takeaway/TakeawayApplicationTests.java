package com.code.takeaway;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
class TakeawayApplicationTests {

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Test
    void contextLoads() {

    }
    @Test
    void  helloWordSend(){ // 第一种hello world模型使用 开发生产者
        rabbitTemplate.convertAndSend("message","你好啊！ 朋友");
    }

    @Test
    void helloWordReceive(String message){ // 第一种hello world模型使用 开发消费者

    }

}
