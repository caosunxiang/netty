package com.example.netty.erupt.sync006;

/**
 * @program: netty
 * @description: 多线程 第六天    syhchronized写法举例
 * @author: 曹孙翔
 * @create: 2019-11-23 14:34
 **/



/**
 * 使用synchronized声明的方法在某些情况下是有弊端的，
 * 比如：A线程调用synchronized同步的方法执行一个很长时间的任务，那么B线程就必须等待比较长的事件才能执行，
 * 这样的情况下可以使用synchronized去优化代码的执行时间，也就是通常所说的减小锁的粒度。
 *
 * synchronized可以使用任意的Object进行加锁，用法比较灵活。
 *
 * 使用synchronized代码块加锁,比较灵活
 * @author jeff
 *
 */
public class ObjectLock {
    public  void method1(){
        synchronized (this){//对象锁
            try {
                System.out.println("do method1..");
                Thread.sleep(2000);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    public void method2(){//类锁
        synchronized (ObjectLock.class){
            try {
                System.out.println("do method2..");
                Thread.sleep(2000);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }
    private Object lock=new Object();
    public void method3(){
        synchronized (lock){
            try {
                System.out.println("do method3..");
                Thread.sleep(20000);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        final ObjectLock objLock=new ObjectLock();
        Thread t1=new Thread(new Runnable() {
            @Override
            public void run() {
                objLock.method1();
            }
        });
        Thread t2=new Thread(new Runnable() {
            @Override
            public void run() {
                objLock.method2();
            }
        });
        Thread t3=new Thread(new Runnable() {
            @Override
            public void run() {
                objLock.method3();
            }
        });
        t1.start();
        t2.start();
        t3.start();
    }
}

