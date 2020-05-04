package com.lxr.core;

import java.util.List;

public interface Service extends MyLifeLine{

    public List<Connector> getConnectors();

    void addConnector(Connector connector);

    void setEngine(Engine engine);
}
