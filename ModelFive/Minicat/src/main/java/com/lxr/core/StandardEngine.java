package com.lxr.core;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lvxinran
 * @date 2020/5/4
 * @discribe
 */
public class StandardEngine implements Engine {

    List<Host> hosts = new ArrayList<>(1);

    @Override
    public void addHost(Host host) {
        hosts.add(host);
    }

    public List<Host> getHosts() {
        return hosts;
    }
}
