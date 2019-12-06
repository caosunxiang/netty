package com.example.netty.erupt.sync002;

/**
 * @program: netty
 * @description: 多线程 第二天   synchronized与多个线程多个锁
 * @author: 曹孙翔
 * @create: 2019-11-23 10:58
 **/



/**
 * 多个线程多个锁
 *
 * 关键字synchronized取得的锁都是对象锁，而不是把一段代码（方法）当做锁，
 * 所以代码中哪个线程先执行synchronized关键字的方法，哪个线程就持有该方法所属对象的锁（Lock），
 *
 * @author jeff
 *
 */
public class MultiThread {
    private static  int num=0;

    public /**static**/  synchronized void printNum(String tag){
        try {
            if (tag.equals("a")){
                num=100;
                System.out.println("tag a, set num over!");
                Thread.sleep(1000);
            }else{
                num=200;
                System.out.println("tag b, set num over!");
            }
            System.out.println("tag  "+tag+",num="+num);
        }catch (Exception e){

        }
    }
    //注意观察run方法输出顺序
    public static void main(String[] args) {
        /**
         * 俩个不同的对象m1/m2：
         * 一个对象一把锁，m1和m2两个对象获得的是自己的那一把对象锁  ，两者没有任何关系，不存在同步问题。
         *
         * 在静态类型的printNum方法上加synchronized关键字，表示锁定.class类，类一级别的锁（独占.class类）。
         * m1和m2两个线程对象在访问printNum时，访问的是同一把锁，
         * 所以最终的结果一定是：要么先执行a的结果，再执行b的：
         * 		tag a, set num over!
         tag a, num = 100
         tag b, set num over!
         tag b, num = 200
         要么是先执行b的，再执行a的：
         tag b, set num over!
         tag b, num = 100
         tag a, set num over!
         tag a, num = 200
         *
         */
        final MultiThread m1=new MultiThread();
        final MultiThread m2=new MultiThread();
        Thread t1=new Thread(new Runnable() {
            @Override
            public void run() {
                m1.printNum("a");
            }
        });
        Thread t2 =new Thread(new Runnable() {
            @Override
            public void run() {
                m2.printNum("b");
            }
        });
        t1.start();
        t2.start();
    }
//    这两个线程之间获得的是自己的那一把锁，多个线程多个锁，各自无影响。
}
