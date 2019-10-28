package com.example.netty.erupt;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @program: netty
 * @description:  在Java SE5 的java.util.concurrent类库中还包含有定义在java.util.concurrent.locks中的显式的互斥机制。
 * Lcok对象必须被显示的创建、锁定、和释放。
 * 因此，它与内间的锁形式相比，代码缺乏优雅性。
 * 但是，对于解决某些类型的问题，它更加灵活。
 * @author: 曹孙翔
 * @create: 2019-10-22 14:13
 **/
public class LockAndUnLock {
    static Lock lock=new ReentrantLock();//创建线程锁

    public static void main(String[] args) {
        new Thread("a"){
            public void run(){
                Thread.yield();//当前线程让步，加快线程切换
                numPrint();
            };
        }.start();
        new Thread("b"){
            public void run(){
                Thread.yield();//当前线程让步，加快线程切换
                numPrint();
            };
        }.start();
    }

    private static  void numPrint(){
        lock.lock();
        try {
        for (int i=0;i<10;i++){
                Thread.sleep(100);
            System.out.println("当前线程"+Thread.currentThread().getName()+":"+i);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }
}
/**
 * 可以看出一个被互斥调用的锁，并使用lock()和unlock()方法在numPrint()内创建了临界资源。
 * 当你在使用Lock对象时，将这里的所示的惯用法内部化是很重要的：
 * 紧接着的对lock()的调用，你必须放再finally子句中带有unlock()的try-finally语句中。
 * 尽管try-finally所需的代码比synchronized关键字要多，但是这也代表了显示的Lock对象的优点之一。
 * 如果在使用synchronized关键字，某些事务失败了，那么就会抛出一个异常。
 * 但是你没有机会去做任何清理工作，以维护系统使其处于良好状态。
 * 有了显示的Lock对象，你就可以使用finally子句将系统维护在正确的状态了。
 *   大体上，当你使用synchronized关键字时，需要写的代码量更少，并且用户错误出现的可能性也会降低，因此通常只有在解决特殊问题时，才使用显示的Lock对象。
 * **/