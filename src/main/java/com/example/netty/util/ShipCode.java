package com.example.netty.util;

import com.mj.courier_station.mapper.DbParcelMapper;
import com.mj.courier_station.pojo.DbParcel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @program: courier_station
 * @description: 出货码生成策略
 * @author: 曹孙翔
 * @create: 2019-09-24 19:20
 **/
@Component
public class ShipCode {
    private  static  Integer num=1;
    @Autowired
    public DbParcelMapper dbParcelMapper;

    @Scheduled(cron ="0 0 0 * * ?" )//每天生成1-99的数字，每晚23点统一清零
    public void clean(){
        num=1;
    }
    public boolean verify(String code,Integer uid){//验证取货码是否重复
        DbParcel dbParcel=dbParcelMapper.getByCode(code,uid);
        if (dbParcel!=null){
            return true;
        }
        return false;
    }
    public String  code(String str){//生成取货码
        Calendar cal=Calendar.getInstance();
        Integer day= cal.get(Calendar.DATE);
        str=str+day+String.format("%02d", num);
        num++;
        if (num==100){
            num=1;
        }
        return str;
    }

    @Scheduled(cron ="0 0 0 * * ?" )
    public void Retention() throws ParseException {
        List<DbParcel> list=dbParcelMapper.getUpAll();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 获取当前时间
        Date date = new Date();
        for (DbParcel dbParcel : list){
            String nowtime = df.format(date);
            // System.out.println(nowtime);
            Date d2 = df.parse(dbParcel.getPUpTime());  //
            Date d1 = df.parse(nowtime);

            long diff = d1.getTime() - d2.getTime();// 这样得到的差值是微秒级别
            long days = diff / (1000 * 60 * 60 * 24);//天
            long hours = (diff - days * (1000 * 60 * 60 * 24))
                    / (1000 * 60 * 60);    //小时
            if (days>1){
                dbParcelMapper.changeRetention(dbParcel.getPId());
            }
            /*long sc = (diff - days * (1000 * 60 * 60 * 24) - hours
                    * (1000 * 60 * 60)) / (1000); // 秒*/
        }
    }
}
