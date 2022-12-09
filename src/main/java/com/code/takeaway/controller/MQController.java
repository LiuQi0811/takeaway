package com.code.takeaway.controller;
import com.code.takeaway.mq.HelloWorldConsumer;
import com.code.takeaway.mq.HelloWorldProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author LiuQi
 * @version 1.0
 * @data 2022/12/8 13:15
 */
@RequestMapping(value = "/mq")
@RestController
public class MQController {
    @Autowired
    private HelloWorldProducer helloWorldProducer;

    @Autowired
    private HelloWorldConsumer helloWorldConsumer;

    @RequestMapping(value = "helloWordSend", method = RequestMethod.GET)
    public void helloWordSend() {
        helloWorldProducer.send();
    }

    @RequestMapping(value = "/helloWordReceive", method = RequestMethod.GET)
    public void helloWordReceive() {
        helloWorldConsumer.receive(new String());
    }
}


