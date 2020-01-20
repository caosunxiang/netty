package com.example.netty.commons;

import com.example.netty.pojo.User;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.net.URLCodec;
import org.apache.commons.collections4.CollectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * @program: netty
 * @description: 测试 Apache Commons （Java世界中最常用的工具类）中的方法
 * @author: 曹孙翔
 * @create: 2019-11-26 09:58
 **/
public class test {
    /***
     * @Author: 曹孙翔
     * @Description:类属性赋值（虽然感觉没啥用，和get/set方法差不多）
     * @Date: 10:27 2019/11/26
     * @Param: [object, name, pwd]
     * @return: void
     **/
    public static void Property(Object object, String name, String pwd) {
        try {
            BeanUtils.setProperty(object, "name", name);
            BeanUtils.setProperty(object, "pwd", pwd);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /***
     * @Author: 曹孙翔
     * @Description:吧对象转换成map（map只能是string，string）类型的 有点鸡肋
     * @Date: 10:33 2019/11/26
     * @Param: [object]
     * @return: java.util.Map
     **/
    public static Map<String, String> describe(Object object) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Map<String, String> map = BeanUtils.describe(object);
        return map;
    }

    /***
     * @Author: 曹孙翔
     * @Description:把map封装进对象 （虽然map的value是string类型的但是依然可以赋值其他类型里，对象没有测试）
     * @Date: 10:47 2019/11/26
     * @Param: [o, map]
     * @return: void
     **/
    public static void populate(Object o, Map<String, String> map) throws InvocationTargetException, IllegalAccessException {
        BeanUtils.populate(o, map);
    }

    /***
     * @Author: 曹孙翔
     * @Description:加密 base64/url：加密后是一致的，但这个url里的另一种加密方式没搞懂就没写，常用的还是md5上两种应该是可逆的，md5应该是不可逆的。
     * @Date: 11:55 2019/11/26
     * @Param: [pwd]
     * @return: void
     **/
    public static void Codec(String pwd) throws DecoderException {
        //string转byte
        //Base64
        byte[] bytes = pwd.getBytes();
        String codec1 = Base64.encodeBase64String(bytes);
        byte[] bytes1 = Base64.decodeBase64(codec1);
        System.err.println("Base64\n转换前：" + Arrays.toString(bytes)/**输出数组元素而非数组地址**/ + "\t转换后：" + codec1 + "\t,解码后:" + Arrays.toString(bytes1));
        //MD5
        String codec2 = DigestUtils.md5Hex(pwd);
        System.err.println("MD5\n转换后：" + codec2);
        //url
        byte[] bytes2 = URLCodec.decodeUrl(bytes);
        System.err.println("URl\n转换后：" + Arrays.toString(bytes2));
    }

    /***
     * @Author: 曹孙翔
     * @Description:对接口collection进行处理。
     * @Date: 15:12 2019/11/26
     * @Param: [map1, map2]
     * @return: void
     **/

    public static void Collections(Collection map1, Collection map2) {
        //判断集合是否为空
        boolean flag = CollectionUtils.isEmpty(map1);
        //交集
        Collection collection1 = CollectionUtils.retainAll(map1, map2);
        //并集
        Collection collection2 = CollectionUtils.union(map1, map2);
        //差集
        Collection collection3 = CollectionUtils.subtract(map1, map2);
        //判等
        boolean flag1 = CollectionUtils.isEqualCollection(map1, map2);
        System.err.println("判断集合是否为空：" + flag + ",判断集合是否相等：" + flag1 + ",集合的交集：" + collection1 + ",集合的并集：" + collection2 + ",集合的差集：" + collection3);
    }


    public static void main(String[] args) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException, DecoderException {
        User user = new User();
        Property(user, "曹孙翔", "csx12345");
        System.err.println(">>>>>>>>>>>>>>" + BeanUtils.getProperty(user, "name") + "<<<<<<<<<<<<<<<<<" + BeanUtils.getProperty(user, "pwd"));
        Map<String, String> map = describe(user);
        map.put("id", "1");
        System.err.println("<><><><><><><><>" + describe(user).toString() + "<><><><><><><><><>");
        populate(user, map);
        System.err.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!" + user.toString() + "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        Codec(user.getPwd());
        Collection collection = new ArrayList();
        collection.add("李");
        collection.add("wen");
        Collection collection1 = new ArrayList();
        collection1.add("娇");
        Collections(collection, collection1);
    }
}
