package com.code.takeaway.current;

import lombok.extern.slf4j.Slf4j;

import java.io.FileReader;

/**
 * @author LiuQi
 * @version 1.0
 * @data 2022/11/24 13:07
 */
@Slf4j
public class DEMO
{
    public static void main(String[] args) {

        //start 与 run
        Thread t = new Thread("TIME"){
            @Override
            public void run() {
                //执行得任务.....
                log.debug(Thread.currentThread().getName());

            }
        };
        // 调用run
        t.run();
        // 调用start
        t.start();
        log.debug("do other things.......");


        // sleep 与 yield
        // 线程优先级
        Runnable task1 = () ->{
            int count = 0;
            for (;;){
                System.out.println("---->1 " + count++);
            }
        };

        Runnable task2 = () ->{
            int count = 0;
            for (;;){
                System.out.println("---->2 " + count++);
            }
        };

        Thread t1 = new Thread(task1,"T1");
        Thread t2 = new Thread(task1,"T2");
        t1.start();
        t2.start();


    }
}
