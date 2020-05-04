package com.lxr.core;

import java.util.List;

public interface Server extends MyLifeLine{

    List<Service> getServices();

    void addAllServices(List<Service> services);

    void addService(Service service);
}
