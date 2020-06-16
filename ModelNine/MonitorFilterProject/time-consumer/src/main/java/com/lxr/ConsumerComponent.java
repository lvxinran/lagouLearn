package com.lxr;

import com.lxr.service.TimeFunction;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Component;

/**
 * @author lvxinran
 * @date 2020/6/17
 * @discribe
 */
@Component
public class ConsumerComponent {

    @Reference
    private TimeFunction timeFunction;

    public String callA() {
        return timeFunction.methodA();
    }
    public String callB() {
        return timeFunction.methodB();
    }
    public String callC() {
        return timeFunction.methodC();
    }

}
