package com.example.netty.erupt.sync006;

/**
 * @program: netty
 * @description: 乐观锁
 * @author: 曹孙翔
 * @create: 2019-11-23 16:58
 **/


/**使用synchronized代码块减小锁的粒度，提高性能**/
public class Optimize {
    public void doLongTimeTask(){
        try {
            System.out.println("当前线程开始：" + Thread.currentThread().getName() +
                    ", 正在执行一个较长时间的业务操作，其内容不需要同步");
            Thread.sleep(2000);
            synchronized (this){
                System.out.println("当前线程：" + Thread.currentThread().getName() +
                        ", 执行同步代码块，对其同步变量进行操作");
                Thread.sleep(1000);
            }
            System.out.println("当前线程结束：" + Thread.currentThread().getName() +
                    ", 执行完毕");
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        final  Optimize otz=new Optimize();
        Thread t1=new Thread(new Runnable() {
            @Override
            public void run() {
                otz.doLongTimeTask();
            }
        },"t1");
        Thread t2=new Thread(new Runnable() {
            @Override
            public void run() {
                otz.doLongTimeTask();
            }
        },"t2");
        t1.start();
        t2.start();
    }
    /**
     * 分析：一个操作内，大部分操作不需要同步，异步即可满足，
     * 而只有其中一小步需要进行同步控制，比如修改数据库的一条数据，
     * 那么仅仅使用代码块对这个操作进行加锁即可。（数据库使用版本号也可以实现乐观锁，
     * update时比对前一步查出来的版本号是否被修改，如果没被修改则执行修改，否则重试）。
     * */
}
