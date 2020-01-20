//package com.example.netty.erupt.sync010;
//
///**
// * @program: netty
// * @description: 多线程    第11天  单例模式和线程安全
// * @author: 曹孙翔
// * @create: 2019-12-13 16:00
// **/
///***
// * @Author: 曹孙翔
// * @Description:懒汉模式-非安全
// * 懒汉模式（线程不安全，可能出现多个Singleton 实例）
// * @Date: 16:35 2019/12/13
// * @Param:
// * @return:
// **/
////public class Singleton {
////    private static  Singleton instance;
////
////    private Singleton(){}
////
////    public  static Singleton getInstance(){
////         if (instance==null){
////             instance=new Singleton();
////         }
////         return instance;
////    }
////}
//    /***
//     * @Author: 曹孙翔
//     * @Description:懒汉模式-安全
//     * 懒汉模式 （线程安全）
//     * @Date: 16:37 2019/12/13
//     * @Param:
//     * @return:
//     **/
////    public class Singleton{
////
////        private static  Singleton instance;
////        private Singleton(){}
////public static  synchronized  Singleton getInstance(){
////            if (instance==null){
////                instance=new Singleton();
////            }
////            return  instance;
////        }
////    }
//
///**饿汉模式*/
//public class Singleton{
//    private static Singleton instance=new Singleton();
//    private Singleton(){};
//    public static  Singleton getInstance(){
//        return  instance;
//    }
//    /**饿汉模式（变种）
//    饿汉（变种，跟第三种差不多，都是在类初始化即实例化instance）*/
//    public class Singleton {
//        private Singleton instance = null;
//        static {
//            instance = new Singleton();
//        }
//        private Singleton (){}
//        public static Singleton getInstance() {
//            return this.instance;
//        }
//    }
//
//
///**静态内部类
//    静态内部类，跟三四有细微差别：
//    Singleton类被装载instance不一定被初始化，因为内部类SingletonHolder没有被主动使用，只有显示调用getInstance才会显示装载SingletonHolder 类，从而实例化instance*/
//
//    public class Singleton {
//        private static class SingletonHolder {
//            private static final Singleton INSTANCE = new Singleton();
//        }
//        private Singleton (){}
//        public static final Singleton getInstance() {
//            return SingletonHolder.INSTANCE;
//        }
//    }
//
//
///**枚举
//    枚举（既可以避免多线程同步问题，还可以防止被反序列化重建对象）*/
//    public enum Singleton {
//        INSTANCE;
//        public void whateverMethod() {
//
//        }
//
//        public static void main(String[] args) {
//            Singleton s = Singleton.INSTANCE;
//            Singleton s2 = Singleton.INSTANCE;
//            System.out.println(s==s2);
//        }
//
//    }
//    /**输出结果：true
//    说明这种方式创建的对象是同一个，因为枚举类中的INSTANCE是static final类型的，只能被实例化一次。对于Enum中每一个枚举实例，都是相当于一个单独的Singleton实例。所以借用 《Effective Java》一书中的话，
//    单元素的枚举类型已经成为实现Singleton的最佳方法*/
//
///***懒汉升级版*/
//    public class Singleton {
//        private volatile static Singleton singleton;
//        private Singleton (){}
//        public static Singleton getSingleton() {
//            if (singleton == null) {
//                synchronized (Singleton.class) {
//                    if (singleton == null) {
//                        singleton = new Singleton();
//                    }
//                }
//            }
//            return singleton;
//        }
//    }
//}
//
