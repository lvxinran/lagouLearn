package com.lxr.core;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lvxinran
 * @date 2020/5/4
 * @discribe
 */
public class StandardService implements Service{


    private List<Connector> connectors = new ArrayList<>(1);

    private Engine engine = null;

    public List<Connector> getConnectors() {
        return connectors;
    }


    public void addConnector(Connector connector){
        connectors.add(connector);
    }


    public Engine getEngine() {
        return engine;
    }

    public void setEngine(Engine engine) {
        this.engine = engine;
    }
}
