package com.example.netty.erupt;

/**
 * @program: netty
 * @description: 并发  车站类
 * @author: 曹孙翔
 * @create: 2019-10-21 13:56
 **/
public class Station extends Thread {

    //构造方法给线程赋值
    public Station(String name) {
        super(name);//给线程名字赋值
    }

    //为了保持票数一直，票数要静态
    static int tick = 20;

    //创建一个静态钥匙
    static Object ob = "aa";//任意值

    //重写run方法，实现买票操作

    @Override
    public void run() {
        while (tick>0){
            synchronized (ob){//这个很重要，必须使用一个锁
                if(tick>0){
                    System.out.println(getName()+"这是第"+tick+"张票");
                    tick--;
                }else {
                    System.out.println("已售罄");
                }
            }
            try {
                sleep(1000);//间隔一秒
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
