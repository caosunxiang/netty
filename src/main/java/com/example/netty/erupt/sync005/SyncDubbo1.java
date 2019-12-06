package com.example.netty.erupt.sync005;

/**
 * @program: netty
 * @description: 多线程学习第五天 synchronized锁重入
 * @author: 曹孙翔
 * @create: 2019-11-23 14:01
 **/

/**
 * synchronized的重入：
 *    当一个线程得到一个对象锁且还未释放锁时，再次请求此对象锁时可以再次得到该对象的锁
 * @author jeff
 *
 */
public class SyncDubbo1 {
    public synchronized void method1() {
        System.out.println("method1..");
        method2();
    }

    public synchronized void method2() {
        System.out.println("method2..");
        try {
            Thread.sleep(1000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        method3();
    }

    public synchronized void method3() {
        System.out.println("method3..");

    }

    public static void main(String[] args) {
        final SyncDubbo1 sd = new SyncDubbo1();
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                sd.method1();
            }
        });
        t1.start();
    }
}
