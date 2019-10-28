package com.example.netty.erupt;

import java.util.ArrayList;
import java.util.concurrent.*;

/**
 * @program: netty
 * @description: 1.4 使用Callable  
 *  Runnable是执行工作的独立任务，但是它不返回任何值。
 * 如果你希望任务在完成时能够返回一个值，那么可以实现Callable接口而不是Runnable接口。
 * 在Java SE 5 中引入的Callable是一种具有类型参数的泛型，它的类型参数表示的是从方法call()中返回的值，
 * 并且必须使用ExecutorService.submit()方法调用它
 * @author: 曹孙翔
 * @create: 2019-10-21 21:58
 **/
public class CallableDemo {
    public static void main(String[] args) {
        class  TaskWithResult implements Callable<String>{
            private int id;
            public TaskWithResult(int id){
                this.id=id;
            }

            @Override
            public String call() throws Exception {
                return "result of Callable "+id;
            }
        }
        ExecutorService exec = Executors.newCachedThreadPool();
        //Future用来可接收线程返回的结果
        ArrayList<Future<String>> results = new ArrayList<Future<String>>();
        for(int i=0;i<5;i++){
            results.add(exec.submit(new TaskWithResult(i)));
        }

        for(Future<String> fs:results){
            try {
                System.out.println(fs.get());
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (ExecutionException e) {

                // TODO Auto-generated catch block
                e.printStackTrace();
            }finally{
                exec.shutdown();
            }
        }
    }
}
