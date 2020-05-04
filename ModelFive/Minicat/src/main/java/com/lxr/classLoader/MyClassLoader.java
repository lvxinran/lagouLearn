package com.lxr.classLoader;

import java.net.URL;
import java.net.URLClassLoader;

/**
 * @author lvxinran
 * @date 2020/5/4
 * @discribe
 */
public class MyClassLoader extends URLClassLoader {

    public MyClassLoader(URL fileURL){
        super(new URL[]{fileURL},null,null);
    }

}
