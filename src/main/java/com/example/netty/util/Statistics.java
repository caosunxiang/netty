package com.example.netty.util;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @program: courier_station
 * @description: 统计辅助类
 * @author: 曹孙翔
 * @create: 2019-10-24 11:22
 **/
@Data

public class Statistics {
    private  String brand;
    private  String count;
    private  String bName;
    private List<Map<String,Object>> data;

}
