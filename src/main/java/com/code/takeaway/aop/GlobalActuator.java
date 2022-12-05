package com.code.takeaway.aop;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
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
    ThreadLocal<Long> startTime = new ThreadLocal<>();
    ConcurrentHashMap<Object, Object> countMap = new ConcurrentHashMap<Object, Object>();

    /**
     * 匹配控制层层通知 这里监控controller下的所有接口
     */
    @Pointcut("execution(* com.code.takeaway.controller.*Controller.*(..))")
    private void allController() {
        log.debug("匹配控制层层通知 这里监控controller下的所有接口......");
    }

    @Before("allController()")
    public void doBefore(JoinPoint joinPoint){
        startTime.set(System.currentTimeMillis());
        Object[] args = joinPoint.getArgs(); // 获取传入目标方法的参数
    }

    /**
     * 只有正常返回才会执行此方法
     * 如果程序执行失败，则不执行此方法
     *
     * @param joinPoint
     * @param returnVal
     */
    @AfterReturning(returning = "returnVal", pointcut = "allController()")
    public void doAfterReturning(JoinPoint joinPoint, Object returnVal) throws Throwable {
        Signature signature = joinPoint.getSignature();
        String declaringName = signature.getDeclaringTypeName();
        String methodName = signature.getName();
        String mapKey = declaringName + methodName;
        log.debug(mapKey);
        // 执行成功则计数加一
        int increase = AtomicCount.getInstance().increase();
        log.debug("执行成功则计数加一 结果{}",increase);
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        synchronized (this) {
            //在项目启动时，需要在Redis中读取原有的接口请求次数
//            if (countMap.size() == 0) {
//                JSONObject jsonObject = RedisUtils.objFromRedis(StringConst.INTERFACE_ACTUATOR);
//                if (jsonObject != null) {
//                    Set<String> strings = jsonObject.keySet();
//                    for (String string : strings) {
//                        Object o = jsonObject.get(string);
//                        countMap.putIfAbsent(string, o);
//                    }
//                }
//            }
        }
        // 如果此次访问的接口不在countMap，放入countMap
        countMap.putIfAbsent(mapKey, 0);
        countMap.compute(mapKey, (key, value) -> (Integer) value + 1);
        synchronized (this) {
            // 内存计数达到30 更新redis
//            if (increase == 30) {
//                RedisUtils.objToRedis(StringConst.INTERFACE_ACTUATOR, countMap, Constants.AVA_REDIS_TIMEOUT);
//                //删除过期时间
//                stringRedisTemplate.persist(StringConst.INTERFACE_ACTUATOR);
//                //计数器置为0
//                AtomicCounter.getInstance().toZero();
//            }
        }
        //log.info("方法执行次数:" + mapKey + "------>" + countMap.get(mapKey));
        //log.info("URI:[{}], 耗费时间:[{}] ms", request.getRequestURI(), System.currentTimeMillis() - startTime.get());
    }

    /**
     * 当接口报错时执行此方法
     */
    @AfterThrowing(pointcut = "allController()")
    public void doAfterThrowing(JoinPoint joinPoint) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        log.info("接口访问失败，URI:[{}], 耗费时间:[{}] ms", request.getRequestURI(), System.currentTimeMillis() - startTime.get());
    }
}
