package com.example.netty.erupt;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @program: netty
 * @description:
 * 1.1 线程池的创建
 *   Java SE 5的java.util.concurrent包中的执行器（Executor）将为你管理Thread对象，
 * 从而简化了并发编程。Executor在客户端和任务执行之间提供了一个间接层；
 * 与客户端直接执行任务不同，这个中介对象将执行任务。
 * Executor允许你管理异步任务的执行，而无须显式的管理线程的生命周期。
 * Executor在Java中启动任务的优选方法。
 * @author: 曹孙翔
 * @create: 2019-10-21 20:38
 **/
public class CachedThreadPool {
    public static void main(String[] args) {
        class MyRunnable implements Runnable{
            private int a = 5;
            //声明线程
            @Override
            public void run() {
                synchronized(this){
                    for(int i=0;i<10;i++){
                        if(this.a>0){
                            System.out.println(Thread.currentThread().getName()+" a的值:"+this.a--);
                        }

                    }
                }
            }

        }
        //创建线程池 管理线程
        ExecutorService exec = Executors.newCachedThreadPool();
        for(int i=0;i<5;i++)
            //启动线程
            exec.execute(new MyRunnable());
        //停止全部的线程
        exec.shutdown();

    }
}
