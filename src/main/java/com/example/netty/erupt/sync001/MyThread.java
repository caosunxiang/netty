package com.example.netty.erupt.sync001;

/**
 * @program: netty
 * @description: 多线程第一天
 * @author: 曹孙翔
 * @create: 2019-11-23 10:37
 **/





/**
  * 多个线程一把锁
  * 
  * 线程安全概念：
  * 	     当多个线程访问某一个类（对象或方法）时，这个对象始终都能表现出正确的行为，
  * 那么这个类（对象或方法）就是线程安全的。
  * 
  * synchronized：可以在任意对象及方法上加锁，而加锁的这段代码称为"互斥区"或"临界区"
  * 
  * 
  * 描述：观察MyThread类的run方法上加锁synchronized和不加锁打印有何区别。
  * 
  * @author jeff
  *
  */
public class MyThread extends  Thread {

    public  Integer count=5;

    @Override
    public synchronized void  run() {

            count--;
            System.out.println(Thread.currentThread().getName() + " count = "+ count);

}


    public static void main(String[] args) {
        MyThread myThread = new MyThread();
        Thread t1 = new Thread(myThread,"t1");
        Thread t2 = new Thread(myThread,"t2");
        Thread t3 = new Thread(myThread,"t3");
        Thread t4 = new Thread(myThread,"t4");
        Thread t5 = new Thread(myThread,"t5");
        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();
    }
}
