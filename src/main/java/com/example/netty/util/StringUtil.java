package com.example.netty.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: courier_station
 * @description: 处理字符串
 * @author: 曹孙翔
 * @create: 2019-09-25 10:16
 **/
public class StringUtil {
   /***
    * @Author: 曹孙翔
    * @Description:切割字符串
    * @Date: 10:20 2019/9/25
    * @Param:
    * @return:
    **/
    public static List<String> divide(String msg){
        List<String >list=new ArrayList<String>();
        msg = msg + ",";
        char a[] = msg.toCharArray();
        Integer c = 0;
        Integer changeCount = 0;
        for (int i = 0; i < a.length; i++) {
            if (a[i] == ',') {
                String string = msg.substring(c, i);
                c = i + 1;
                System.out.println(string);
                list.add(string);
                changeCount++;
            }
        }
        return list;
    }
}
