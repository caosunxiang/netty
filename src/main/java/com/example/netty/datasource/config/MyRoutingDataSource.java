package com.example.netty.datasource.config;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.lang.Nullable;

/**
 * @program: netty
 * @description: 获取路由key
 * @author: 曹孙翔
 * @create: 2019-10-23 15:45
 **/
public class MyRoutingDataSource extends AbstractRoutingDataSource {
    @Nullable
    @Override
    protected Object determineCurrentLookupKey() {
        return DBContextHolder.get();
    }
}
