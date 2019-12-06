package com.example.netty.erupt.sync003;

/**
 * @program: netty
 * @description: 多线程 第三天  一个对象多个方法一把锁
 * @author: 曹孙翔
 * @create: 2019-11-23 11:24
 **/


/**
 * 一个对象多个方法一把锁
 *
 * 对象锁的同步和异步问题

 * @author jeff
 *
 */
public class MyObject {
    /**
     * 同步方法
     * */
    public synchronized  void method1(){
        try {
            System.out.println(Thread.currentThread().getName());
            Thread.sleep(4000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }
    /**
     * 异步方法
     * */
    public /*synchronized*/  void method2(){
        System.out.println(Thread.currentThread().getName());
    }

    public static void main(String[] args) {
        final MyObject mo=new MyObject();

        /**
         * 分析：
         * t1线程先持有mo对象的Lock锁，t2线程可以以异步的方式调用mo中的非synchronized修饰的方法，所以两个方法几乎同时执行打印结果。
         *
         * t1线程先持有mo对象的Lock锁，t2线程如果在这个时候调用mo中的同步（synchronized）方法则需等待t1释放mo对象锁后才能执行method2同步方法打印结果。
         */
        Thread t1=new Thread(new Runnable() {
            @Override
            public void run() {
                mo.method1();
            }
        },"t1");

        Thread t2=new Thread(new Runnable() {
            @Override
            public void run() {
                mo.method2();
            }
        },"t2");
        t1.start();
        t2.start();;
    }
    /**
     * 注意两个概念：
     *
     * 1 同步方法：加了synchronized的方法，存在锁竞争。
     *
     * 2 异步方法：不加synchronized的方法，不存在锁竞争。
     *
     *
     *
     * 如果我们把method2的synchronized去掉，打印结果依然是t1和t2，不同的是两者几乎是同时打印，不存在像同步一样的先后顺序。
     * */
}
