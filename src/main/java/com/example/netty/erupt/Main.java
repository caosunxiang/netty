package com.example.netty.erupt;

/**
 * @program: netty
 * @description: 测试方法
 * @author: 曹孙翔
 * @create: 2019-10-21 14:07
 **/
public class Main {
    public static void main(String[] args) {
        //实例化站台对象，并为每一个站台取名字
        Station station1=new Station("窗口1");
        Station station2=new Station("窗口2");
        Station station3=new Station("窗口3");

        // 让每一个站台对象各自开始工作
        station1.start();
        station2.start();
        station3.start();

    }
}
