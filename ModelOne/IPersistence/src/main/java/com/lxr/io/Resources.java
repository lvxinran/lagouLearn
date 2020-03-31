package com.lxr.io;

import java.io.InputStream;

/**
 * @author lvxinran
 * @date 2020/3/27
 * @discribe
 */
public class Resources {
    public static InputStream getResourceAsStream(String path){
        InputStream resourceAsStream = Resources.class.getClassLoader().getResourceAsStream(path);
        return resourceAsStream;
    }
}
