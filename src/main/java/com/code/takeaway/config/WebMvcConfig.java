package com.code.takeaway.config;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * @author LiuQi
 * @version 1.0
 * @data 2022/11/24 16:55
 */
@Slf4j
@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport
{
    /**
     * 设置静态资源映射
     * @param registry
     */
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {

        log.info("开始进行静态资源映射...");
        registry.addResourceHandler("/backend/**").addResourceLocations("classpath:/backend/");
        log.info("设置静态资源映射入参"+ JSON.toJSON(registry));
    }
}
