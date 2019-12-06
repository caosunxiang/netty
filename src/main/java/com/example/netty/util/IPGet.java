package com.example.netty.util;

import org.apache.http.HttpRequest;

import javax.servlet.http.HttpServletRequest;

/**
 * @program: netty
 * @description: 获取请求ip
 * @author: 曹孙翔
 * @create: 2019-12-02 10:37
 **/
public class IPGet {
    public static String IPGet(HttpServletRequest request){
        /***
         * @Author: 曹孙翔
         * @Description:获取请求ip地址
         * @Date: 10:39 2019/12/2
         * @Param: [request]
         * @return: java.lang.String
         **/
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip.indexOf(",") != -1) {
            String[] ips = ip.split(",");
            ip = ips[0].trim();
        }
        return ip;
    }
}
