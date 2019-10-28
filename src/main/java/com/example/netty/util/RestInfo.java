package com.example.netty.util;

import lombok.Data;

/**
 * @ClassName: RestInfo
 * @Description: 数据返回的同一类
 * @Author: Dragon
 * @Data:2019/9/12 9:47
 */
@Data
public class RestInfo {
    private Boolean status;
    private Integer code;
    private String message;
    private Object databind;

    public RestInfo() {
        super();
    }

    public RestInfo(Boolean status, Integer code, String message, Object databind) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.databind = databind;
    }
}
