package com.code.takeaway.algorithm;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author LiuQi
 * @version 1.0
 * @data 2022/12/13 8:56
 * 冒泡排序
 */
@Slf4j
public class BubbleSort
{
    public static void main(String[] args) {
        int [] arrays = new int[]{10,21,8,16,7,23,12};
        log.info("未排序之前的数组 {}",arrays);
        // 获取length长度 -1
        for (int i = 0; i < arrays.length-1; i++) {
            for (int j = 0; j < arrays.length-1-i; j++) {
                if(arrays[j] > arrays[j+1]){
                    int temp = arrays[j];
                    arrays[j] = arrays[j+1];
                    arrays[j+1] = temp;
                }
            }
        }
        List<Integer> integers = new ArrayList<>();
        for (int array : arrays) {
            integers.add(array);
        }
        log.info("冒泡排序的结果是 {}" ,integers);
    }



}
