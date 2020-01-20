package com.example.netty.pojo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;

/**
 * @program: netty
 * @description: 用户表
 * @author: 曹孙翔
 * @create: 2019-10-10 17:11
 **/
@Data
@RequiredArgsConstructor(staticName = "sunsfan")
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor
@ApiModel("用户实体")
public class User implements Serializable {
    @NonNull
    @ApiModelProperty("用户id")
    private Integer id;
    @NonNull
    private String name;
    @NonNull
    private String pwd;
    private short connected;
    private Long mostsignbits;
    private Long leastsignbits;
    private Role role;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", pwd='" + pwd + '\'' +
                ", connected=" + connected +
                ", mostsignbits=" + mostsignbits +
                ", leastsignbits=" + leastsignbits +
                ", role=" + role +
                '}';
    }
}
