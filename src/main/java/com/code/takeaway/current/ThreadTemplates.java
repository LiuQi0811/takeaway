package com.code.takeaway.current;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * 线程
 * 作者： 刘奇
 */
@Slf4j
public class ThreadTemplates
{
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        methodOne();
        methodTwo();
        methodThree();
        lambdaMethod();
    }


    /**
     * 方法一，直接使用 Thread
     */
    public static void methodOne(){
        // 创建线程对象
        Thread thread = new Thread(){
            @Override
            public void run() {
                // 这里是要执行的任务.......
            }
        };
        // 启动线程
        thread.start();


        // 构造方法的参数是给线程指定名字，推荐
        Thread t = new Thread("线程1"){
            @Override
            public void run() {
                // 这里是要执行的任务.......
                log.debug("hello 线程1");
                System.out.println("hello 线程1");
            }
        };
        // 启动线程
        t.start();
    }


    /**
     * 方法二，使用 Runnable 配合 Thread
     * 把【线程】和【任务】（要执行的代码）分开
     * Thread 代表线程
     * Runnable 可运行的任务（线程要执行的代码）
     */
    public static void methodTwo(){
        Runnable runnable = new Runnable(){
            @Override
            public void run() {
                // 这里是要执行的任务.......
            }
        };
        // 创建线程对象
        Thread t = new Thread(runnable);
        // 启动线程
        t.start();

        // 创建任务对象
        Runnable task =new Runnable() {
            @Override
            public void run() {
                // 这里是要执行的任务.......
                System.out.println("任务执行中。。。。。。。");
            }
        };

        // 参数1 是任务对象; 参数2 是线程名字，推荐
        Thread t2 = new Thread(task, "XIANCHENG2");
        // 启动线程
        t2.start();

    }


    /**
     * 方法三，FutureTask 配合 Thread
     * FutureTask 能够接收 Callable 类型的参数，用来处理有返回结果的情况
     */
    public static void methodThree() throws ExecutionException, InterruptedException {
        // 创建任务对象
        FutureTask<Integer> task = new FutureTask<>(() ->{
            log.debug("FutureTask能够接收 Callable 类型的参数");
            return  100;
        });
        // 参数1 是任务对象; 参数2 是线程名字，推荐
        new Thread(task,"FutureTask").start();

        // 主线程阻塞，同步等待 task 执行完毕的结果
        Integer result = task.get();
        log.debug("结果是：{}",result);
    }
    /**
     * Java 8 使用 lambda 精简代码
     */
    public static void lambdaMethod(){
        // 创建任务对象
        Runnable task = ()-> System.out.println("Java 8 使用 lambda 精简代码");
        // 参数1 是任务对象; 参数2 是线程名字，推荐
        Thread thread = new Thread(task,"Lambda");
        // 启动线程
        thread.start();

    }


}


