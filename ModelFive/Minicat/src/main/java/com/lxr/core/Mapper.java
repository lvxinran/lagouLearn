package com.lxr.core;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lvxinran
 * @date 2020/5/4
 * @discribe
 */
public class Mapper {

    public Server server = null;

    public List<Host> hosts = new ArrayList<>(1);

    public Wrapper getServletByURL(String url){
        String contextName = url.split("/")[1];
        int i = url.indexOf("/", url.indexOf("/") + 1);
        String uri = url.substring(i);
        Context context = hosts.get(0).getContext(contextName);
        if(context!=null){
            List<Wrapper> wrappers = context.getWrappers();
            for(Wrapper wrapper:wrappers){
                if(wrapper.getUrl().equals(uri)){
                    return wrapper;
                }
            }
        }
        return null;
    }

    public List<Host> getHosts() {
        return hosts;
    }
    public void addHosts(Host host){
        hosts.add(host);
    }
}
