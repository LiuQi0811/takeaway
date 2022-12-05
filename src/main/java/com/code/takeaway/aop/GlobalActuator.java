package com.code.takeaway.aop;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.code.takeaway.entity.CountApi;
import com.code.takeaway.mapper.CountApiMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author LiuQi
 * @version 1.0
 * @data 2022/12/5 9:29
 */

@Component
@Aspect
@Slf4j
public class GlobalActuator {
    @Resource
    private CountApiMapper apiMapper;
    ThreadLocal<Long> startTime = new ThreadLocal<>();
    /**
     * 匹配控制层层通知 这里监控controller下的所有接口
     */
    @Pointcut("execution(* com.code.takeaway.controller.*Controller.*(..))")
    private void allController() {
        log.debug("匹配控制层层通知 这里监控controller下的所有接口......");
    }

    /**
     * 在接口原有的方法执行前，将会首先执行此处的代码
     *
     * @param joinPoint
     */
    @Before("allController()")
    public void doBefore(JoinPoint joinPoint) {
        startTime.set(System.currentTimeMillis());
        joinPoint.getArgs(); // 获取传入目标方法的参数
        log.info("获取传入目标方法的参数...{}", joinPoint.getArgs());
    }

    /**
     * 只有正常返回才会执行此方法
     * 如果程序执行失败，则不执行此方法
     *
     * @param returnVal
     */
    @AfterReturning(returning = "returnVal", pointcut = "allController()")
    public synchronized void doAfterReturning(Object returnVal) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        log.info(" api 名称 " + request.getRequestURI());
        // 获取接口请求信息
        CountApi countApi = this.queryCountApiInfoByApi(request.getRequestURI());
        log.info("获取接口请求信息  {}", JSON.toJSON(countApi));
        // 执行成功则计数加一
        int increase = AtomicCount.getInstance().increase();
        if (countApi == null) {
            CountApi data = CountApi.builder()
                    .id(UUID.randomUUID().toString())
                    .api(request.getRequestURI())
                    .num(Integer.toUnsignedLong(increase))
                    .build();
            apiMapper.insert(data);
        } else {

            CountApi  data = null;
            if (increase == 1) { //为了防止 重新部署 重启项目 重新赋值进行判断
                  data = CountApi.builder()
                        .id(countApi.getId())
                        .num(countApi.getNum()+Integer.toUnsignedLong(increase))
                        .build();
            } else {
                  data = CountApi.builder()
                        .id(countApi.getId())
                        .num(countApi.getNum()+1)
                        .build();
            }
            apiMapper.updateById(data);

        }

        log.info("api请求成功加一 结果 {} 第{}次", request.getRequestURI(), increase);


    }

    /**
     * 当接口报错时执行此方法
     */
    @AfterThrowing(pointcut = "allController()")
    public void doAfterThrowing(JoinPoint joinPoint) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        log.info("接口访问失败，URI:[{}], 耗费时间:[{}] ms", request.getRequestURI(), System.currentTimeMillis() - startTime.get());
    }


    /**
     * 根据 api 获取信息
     *
     * @param api
     * @return
     */
    private CountApi queryCountApiInfoByApi(String api) {
        LambdaQueryWrapper<CountApi> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CountApi::getApi, api);
        return apiMapper.selectOne(queryWrapper);
    }


}
