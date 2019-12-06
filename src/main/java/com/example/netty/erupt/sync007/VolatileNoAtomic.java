package com.example.netty.erupt.sync007;

/**
 * @program: netty
 * @description: 多线程    第7天        AutomicInteger原子操作
 * @author: 曹孙翔
 * @create: 2019-11-26 16:22
 **/


import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * volatile关键字不具备synchronized关键字的原子性（同步）
 * @author jeff
 *
 */


public class VolatileNoAtomic extends Thread{
    //private static volatile int count;
   private static AtomicInteger count = new AtomicInteger(0);

    private static void addCount(){
        for (int i = 0; i < 1000; i++) {
            // count++;
            count.incrementAndGet();
        }
        System.out.println(count);
    }
    public void run(){
        addCount();
    }

    public static void main(String[] args) {
        VolatileNoAtomic[] arr=new VolatileNoAtomic[100];
        for (int i = 0; i < 10; i++) {
            arr[i]=new VolatileNoAtomic();
        }
        for (int i = 0; i < 10; i++) {
            arr[i].start();
        }
    }
}

