package com.code.takeaway.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * @author LiuQi
 * @version 1.0
 * @data 2022/12/5 8:51
 */
@Slf4j
@RestController
@RequestMapping(value = "/test")
public class TestController {

    @RequestMapping(value = "/message",method = RequestMethod.GET)
    public String message(HttpServletResponse response){
        response.setCharacterEncoding("UTF8");
        return "Hello World";
    }


}
