package com.example.netty.erupt.sync008;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: netty
 * @description: 多线程   第九天      多线程间的通信
 * @author: 曹孙翔
 * @create: 2019-11-27 09:51
 **/

/**
 * 定义t1和t2两个线程：
 * t1线程往list中加入元素
 * t2线程发现list元素个数为5时抛出异常，停止t2线程
 *
 * @author jeff
 * @param args
 */
public class ListAdd1 {
    private volatile static List list=new ArrayList();

    public void add(){
        list.add("bjsxt");
    }
    public int size(){
        return  list.size();
    }

    public static void main(String[] args) {
        final ListAdd1 list1=new ListAdd1();

        Thread t1=new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i < 10; i++) {
                        list1.add();
                        System.out.println("当前线程："+Thread.currentThread().getName()+"添加了一个元素..");
                        Thread.sleep(500);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"t1");
        Thread t2=new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    if (list1.size()==5){
                        System.out.println("当线程收到通知："+Thread.currentThread().getName()+"   list size=5   停止线程");
                        throw  new  RuntimeException();
                    }
                }
            }
        },"t2");
        t1.start();
        t2.start();
    }
}
