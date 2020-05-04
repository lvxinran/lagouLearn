package com.lxr.core;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lvxinran
 * @date 2020/5/4
 * @discribe
 */
public class Context {
    private String name;

    private List<Wrapper> wrappers = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Wrapper> getWrappers() {
        return wrappers;
    }
    public void addWrapper(Wrapper wrapper){
        wrappers.add(wrapper);
    }

}
