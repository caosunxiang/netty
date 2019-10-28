package com.example.netty.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author yuan
 * @date 2019/3/7 0007
 */
@Component
public class FileProperties {
    @Autowired
    private Environment env;
    public static String mapping;
    public static String address;
    public static String returnUrl;

    @PostConstruct
    public void readConfig() {
        mapping=env.getProperty("upload.mapping");
        address=env.getProperty("upload.address");
        returnUrl=env.getProperty("upload.returnUrl");
    }

}
