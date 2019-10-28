package com.example.netty.mapper;

import com.example.netty.pojo.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper {
    public User login(@Param("name")String name,@Param("pwd")String pwd);

    public User getUser(@Param("name")String name);
}
