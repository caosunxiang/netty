package com.example.netty.erupt.sync006;

/**
 * @program: netty
 * @description: 改变常量锁
 * @author: 曹孙翔
 * @create: 2019-11-23 15:42
 **/
public class ChangeLock {
       private String lock="lock";
       private void method(){
           synchronized (lock){
               try {
                   System.out.println("当前线程 : "  + Thread.currentThread().getName() + "开始");
                   lock="change lock";
                   Thread.sleep(2000);
                   System.out.println("当前线程 : "  + Thread.currentThread().getName() + "结束");
               }catch (InterruptedException e){
                   e.printStackTrace();
               }
           }
       }

    public static void main(String[] args) {
           final ChangeLock changeLock=new ChangeLock();
           Thread t1=new Thread(new Runnable() {
               @Override
               public void run() {
                   changeLock.method();
               }
           },"t1");
           Thread t2=new Thread(new Runnable() {
               @Override
               public void run() {
                   changeLock.method();
               }
           },"t2");
           t1.start();
           try {
               Thread.sleep(100);
           }catch (InterruptedException e){
               e.printStackTrace();
           }
           t2.start();
    }
    /**
     * 在t1线程执行的过程中，t1所持有的锁常量“lock“，被改变，导致锁被突然释放，t2线程获得了锁。
     *
     * lock = "change lock";
     * */
}
