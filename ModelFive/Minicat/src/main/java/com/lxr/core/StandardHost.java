package com.lxr.core;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lvxinran
 * @date 2020/5/4
 * @discribe
 */
public class StandardHost implements Host {
    private String appBase;

    private List<Context> contexts= new ArrayList<>(1);

    public String getAppBase() {
        return appBase;
    }

    @Override
    public Context getContext(String contextName) {
        for(Context context:contexts){
            if(context.getName().equals(contextName)){
                return context;
            }
        }
        return null;
    }

    public void setAppBase(String appBase) {
        this.appBase = appBase;
    }

    @Override
    public void addContext(Context context) {
        contexts.add(context);
    }

}
