package com.example.netty.erupt.sync009;

/**
 * @program: netty
 * @description: 多线程      第10天     ThreadLocal
 * @author: 曹孙翔
 * @create: 2019-12-13 15:07
 **/
public class ConnThreadLocal {
    public  static ThreadLocal<String> th=new ThreadLocal<>();

    public void setTh(String value){
        th.set(value);
    }

    public void getTh(){
        System.out.println(Thread.currentThread().getName()+":"+this.th.get());
    }

    public static void main(String[] args) {
        final  ConnThreadLocal ct=new ConnThreadLocal();
        Thread t1=new Thread(new Runnable() {
            @Override
            public void run() {
                ct.setTh("张三");
                ct.getTh();
            }
        },"t1");
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    ct.getTh();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "t2");
        t1.start();
        t2.start();
    }
}
/**这个例子很简单， * 就是t1设置了ThreadLocal变量，可以获取得到，
 * 而t2获取为null，因为对ThreadLocal来说，
其放置的变量时相对于每个线程独立一份。*/