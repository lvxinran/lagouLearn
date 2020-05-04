package com.lxr.core;

public interface Host extends MyLifeLine{


    String getAppBase();

    Context getContext(String contextName);

    void setAppBase(String appBase);

    void addContext(Context context);
}
