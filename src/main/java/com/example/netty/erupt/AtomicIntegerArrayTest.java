package com.example.netty.erupt;

import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 * @program: netty
 * @description: 当前可用的原子数组有：
 * AtomicIntegerArray、AtomicLongArray和AtomicReferenceArray，分别表示整数数组、long型数组和普通对象数组。
 * @author: 曹孙翔
 * @create: 2019-10-22 15:43
 **/
public class AtomicIntegerArrayTest {

    /**
        AtomicIntegerArray本质上是对int[]类型的封装。使用Unsafe类通过CAS的方式控制int[]在多线程下的安全性。它提供了以下几个核心API：
    //获得数组第i个下标的元素
    public final int get(int i)
    //获得数组的长度
    public final int length()
    //将数组第i个下标设置为newValue，并返回旧的值
    public final int getAndSet(int i, int newValue)
    //进行CAS操作，如果第i个下标的元素等于expect，则设置为update，设置成功返回true
    public final boolean compareAndSet(int i, int expect, intupdate)
    //将第i个下标的元素加1
    public final int getAndIncrement(int i)
    //将第i个下标的元素减1
    public final int getAndDecrement(int i)
    //将第i个下标的元素增加delta（delta可以是负数）
    public final int getAndAdd(int i, int delta)
     */



    static AtomicIntegerArray integerArray = new AtomicIntegerArray(10);

    public static class AddThread implements Runnable{

        int j = 0;

        @Override
        public void run() {
            for(int i=0;i<10000;i++){
                //指定数组下标元素加一
                integerArray.getAndIncrement(i%integerArray.length());
            }

        }

    }

    public static void main(String[] args) {

        Thread[] ts = new Thread[10];
        for(int i=0;i<10;i++){
            ts[i] = new Thread(new AddThread());
        }

        for(int i=0;i<10;i++){
            ts[i].start();
        }
        for(int i=0;i<10;i++){
            try {
                ts[i].join();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        System.out.println(integerArray);
    }
}
