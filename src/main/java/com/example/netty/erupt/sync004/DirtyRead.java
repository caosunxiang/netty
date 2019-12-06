package com.example.netty.erupt.sync004;

/**
 * @program: netty
 * @description: 多线程  第四天  使用synchronized避免DirtyRead
 * @author: 曹孙翔
 * @create: 2019-11-23 11:55
 **/
public class DirtyRead {
    private String username="jeff";
    private String password="123";

    public synchronized  void setValue(String username,String password){
        this.username=username;
        try {
            Thread.sleep(2000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        this.password=password;
        System.out.println("setValue最终结果：username = " + username + " , password = " + password);
        }
    /**
     * synchronized,可以用来保证setValue和getValue方法能够同步
     * */
    public /*synchronized*/ void getValue(){
        System.out.println("getValue方法得到：username = " + this.username + " , password = " + this.password);
    }

    public static void main(String[] args) throws Exception {
        final DirtyRead dr=new DirtyRead();
        Thread t1=new Thread(new Runnable() {
            @Override
            public void run() {
                dr.setValue("z3","456");
            }
        });
        t1.start();
        Thread.sleep(1000);

        dr.getValue();
    }

    /**
     *setValue方法没有执行完成前，getvalue就获取结果，那么最终得到的就是没设置的数据，
     * 如password是123，我们可以在getValue上加上synchronized关键词进行控制，
     * 当t1线程进行setValue设置时拿到了dr对象锁，执行完以后，主线程dr.getValue()方法才获取到被释放的对象锁
     *  避免了脏读数据的产生。
     * */
}
