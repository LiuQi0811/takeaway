package com.code.takeaway.config;

import com.alibaba.fastjson.JSON;
import com.code.takeaway.common.JacksonObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

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

    /**
     * 扩展mvc框架的消息转换器
     * @param converters
     */
    @Override
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        log.info("扩展消息转换器...");
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();// 创建消息转换器对象
        messageConverter.setObjectMapper(new JacksonObjectMapper());// 设置对象转换器，底层使用Jackson将Java对象转为json
        converters.add(0,messageConverter);// 将上面的消息转换器对象追加到mvc框架的转换器集合中

    }
}
