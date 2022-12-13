package com.code.takeaway;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;

/**
 * @author LiuQi
 * @version 1.0
 * @data 2022/12/8 14:14
 */
//@SpringBootTest(classes = TakeawayApplication.class)
@Slf4j
@RunWith(SpringRunner.class)
public class TestRabbitMQ {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    // hello world
    @Test
    public void testHello() {
        rabbitTemplate.convertAndSend("hello", "hello world");
    }

    // work
    @Test
    public void testWork() {
        for (int i = 1; i < 8; i++) {
            rabbitTemplate.convertAndSend("work", "work模型" + i);
        }
    }

    // fanout 广播
    @Test
    public void testFanout() {
        rabbitTemplate.convertAndSend("logs", "", "Fanout的模型发送的消息");
    }

    //route 路由模式
    @Test
    public void testRoute() {
        rabbitTemplate.convertAndSend("directs", "error", "发送info的key的路由信息");
    }

    //topic 动态路由  订阅模式
    @Test
    public void testTopic() {
        rabbitTemplate.convertAndSend("topics", "product.save.add", "produce.save.add 动态路由消息");
    }


    @Test
    public void testSimpleQueue() {
        String queueName = "simple.queue"; // 队列名称
        String message = "hello ,spring amqp!"; // 消息名称
        rabbitTemplate.convertAndSend(queueName, message);
    }

    @Test
    public void testWorkQueue() {
        // 循环50次
        for (int i = 1; i <= 50; i++) {
            String queueName = "work.queue"; // 队列名称
            String message = "hello ,work queue  amqp!" + i; // 消息名称
            rabbitTemplate.convertAndSend(queueName, message);
        }
    }


    @Test
    public void testSendFanoutExchange() {
        String exchangeName = "amqp.fanout"; // 交换机名称
        String message = "hello , every one!"; // 消息
        rabbitTemplate.convertAndSend(exchangeName, "", message); //发送消息
    }

    @Test
    public void testSendDirectExchange() {
        String exchangeName = "amqp.direct"; // 交换机名称
        String message = "hello , red!"; // 消息
        rabbitTemplate.convertAndSend(exchangeName, "red", message); // 发送消息
    }

    @Test
    public void testSendTopicExchangeNews(){
        String exchangeName = "amqp.topic";// 交换机名称
        String message = "北京时间12月11日，世界杯1/4决赛全部结束，本届世界杯的最终四强也已产生。\n" +
                "卡塔尔世界杯四强：\n" +
                "上半区：阿根廷、克罗地亚\n" +
                "下半区：法国、摩洛哥"; //新闻消息
        rabbitTemplate.convertAndSend(exchangeName,"china.news",message);
    }
    @Test
    public void testSendTopicExchangeWeather(){
        String exchangeName = "amqp.topic";// 交换机名称
        String message = "台儿庄区气象台2022年12月10日16时23分继续发布寒潮蓝色预警信号：受强冷空气影响，预计10到11日我区将出现寒潮天气。气温将明显下降，我区大部分镇（街）的过程降温幅度8到10℃，" +
                "最低气温-6℃左右，出现在11日早晨。请注意防范！（" +
                "预警信息来源：国家预警信息发布中心）"; ///天气消息
        rabbitTemplate.convertAndSend(exchangeName,"china.weather",message);
    }

    @Test
    public void testSendObjectQueue(){
        String exchangeName = "object.queue";
         HashMap<String, Object> objectObjectHashMap = new HashMap<>();
         objectObjectHashMap.put("name","张天爱");
         objectObjectHashMap.put("address","黑龙江省绥化市");
         objectObjectHashMap.put("description","张天爱（Crystal），本名张娇，出生于黑龙江省绥化市，中国内地影视女演员。2009年，张天爱因一次偶然机会出演了影视处女作《落樱》。此后，她以广告模特身份踏入演艺圈。2015年，凭借抗战剧《二炮手》获得第2届横店影视文荣奖最佳女配角奖");
         rabbitTemplate.convertAndSend(exchangeName,objectObjectHashMap);
    }


}
