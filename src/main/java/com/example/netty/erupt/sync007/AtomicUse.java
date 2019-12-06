package com.example.netty.erupt.sync007;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @program: netty
 * @description: 多个addAndGet在一个自定义的方法内部是非原子的，需要同步对象锁修饰
 * @author: 曹孙翔
 * @create: 2019-11-26 17:19
 **/
public class AtomicUse {
    private static AtomicInteger count=new AtomicInteger(0);
    /**synchronized*/
    public synchronized int multiAdd(){
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        count.addAndGet(1);
        count.addAndGet(2);
        count.addAndGet(3);
        count.addAndGet(4);//+10
        return  count.get();
    }

    public static void main(String[] args) {
        final AtomicUse au=new AtomicUse();

        List<Thread> ts=new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            ts.add(new Thread(new Runnable() {
                @Override
                public void run() {
                    System.out.println(au.multiAdd());
                }
            }));
        }
        for (Thread t:ts){
            t.start();
        }
    }
}
