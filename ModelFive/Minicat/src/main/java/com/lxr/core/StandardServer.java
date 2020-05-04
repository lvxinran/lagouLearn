package com.lxr.core;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lvxinran
 * @date 2020/5/4
 * @discribe
 */
public class StandardServer implements Server {

    private List<Service> services = new ArrayList<>(1);

    public List<Service> getServices() {
        return services;
    }

    @Override
    public void addAllServices(List<Service> services) {
        this.services.addAll(services);
    }

    public void addService(Service service){
        services.add(service);
    }
}
