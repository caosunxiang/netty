package com.example.netty.erupt.sync008;

import com.example.netty.erupt.sync006.ObjectLock;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @program: netty
 * @description:
 * @author: 曹孙翔
 * @create: 2019-11-27 10:51
 **/




/**
 * wait notfiy 方法，wait释放锁，notfiy不释放锁
 * @author jeff
 *
 */
public class ListAdd2 {
    private volatile static List list=new ArrayList();
    public void add (){
        list.add("bjsxt");
    }
    public int  size(){
        return list.size();
    }

    public static void main(String[] args) {
        final ListAdd2 list2=new ListAdd2();

// 1 实例化出来一个 lock
        // 当使用wait 和 notify 的时候 ， 一定要配合着synchronized关键字去使用

    //    final Object lock= new ObjectLock();

        final CountDownLatch countDownLatch=new CountDownLatch(1);
        
        Thread t1=new Thread(new Runnable() {
            @Override
            public void run() {
                //synchronized (lock){
                    try {
                        for (int i = 0; i < 10; i++) {
                            list2.add();
                            System.out.println("当前线程："+ Thread.currentThread().getName() + "添加了一个元素..");
                            Thread.sleep(500);
                            if (list2.size()==5){
                                System.out.println("已经发出通知");
                                countDownLatch.countDown();
 //                               lock.notify();
                            }
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            //}
        },"t1");
        Thread t2=new Thread(new Runnable() {
            @Override
            public void run() {
                //synchronized (lock){
                    if (list2.size()!=5){
                        try {
                            System.out.println("t2进入...");
                            //lock.wait();
                            countDownLatch.await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("当前线程：" + Thread.currentThread().getName() + "收到通知线程停止..");
                    throw new RuntimeException();
                }
            //}
        },"t2");
        t2.start();
        t1.start();
    }
    /**
     *  t2线程先执行，执行到await方法时释放锁，然后t1获得锁，
     *  当t1添加5个元素后，调用notify方法唤醒t2线程，但是notify方法并不释放锁，
     *  所以t2线程此时并不能得到锁，直到t1线程添加完10个元素以后释放锁，
     *  t2线程await才不再阻塞，打印“收到通知线程停止”。
     * **/


/**
 * 相对于Object类的notify来说，countDown这个方法是实时通信，而notify由于不释放锁，
 * 所以其他线程即便收到了唤醒线程的通知却得不到锁，无法立马执行。
 *
 *
 * */


}
