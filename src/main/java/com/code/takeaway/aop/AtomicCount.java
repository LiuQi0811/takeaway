package com.code.takeaway.aop;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author LiuQi
 * @version 1.0
 * @data 2022/12/5 9:19
 * 原子计数类
 */

public class AtomicCount {
    private static final AtomicCount atomicCount = new AtomicCount();

    private AtomicCount() {

    }

    public static AtomicCount getInstance() { // 单列模式
        return atomicCount;
    }

    private static AtomicInteger count = new AtomicInteger();

    public int getValue() {
        return count.get();
    }

    public int increase() {
        return count.incrementAndGet();
    }

    public int decrease() {
        return count.decrementAndGet();
    }

    public void toZero() { // 清零
        count.set(0);
    }
}
