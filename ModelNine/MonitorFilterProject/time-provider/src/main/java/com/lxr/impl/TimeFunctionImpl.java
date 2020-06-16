package com.lxr.impl;

import com.lxr.service.TimeFunction;
import org.apache.dubbo.config.annotation.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author lvxinran
 * @date 2020/6/17
 * @discribe
 */
@Service
public class TimeFunctionImpl implements TimeFunction {
    @Override
    public String methodA() {
        try {
            int random = (int)(Math.random()*100);
            Thread.sleep(random);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("A执行");
        return "A执行";
    }

    @Override
    public String methodB() {
        try {
            int random = (int)(Math.random()*100);
            Thread.sleep(random);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("B执行");
        return "B执行";
    }

    @Override
    public String methodC() {
        try {
            int random = (int)(Math.random()*100);
            Thread.sleep(random);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("C执行");
        return "C执行";
    }
}
