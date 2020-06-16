package com.lxr;

import com.lxr.service.TimeFunction;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author lvxinran
 * @date 2020/6/17
 * @discribe
 */
public class TimeConsumer {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ConsumerConfiguration.class);
        context.start();
        ConsumerComponent service = context.getBean(ConsumerComponent.class);
        ExecutorService executorService = Executors.newFixedThreadPool(16);
        while (true) {
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    for (int i=0;i<120;i++){
                        String  msg =  service.callA();

                    }
                }
            });
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    for (int i=0;i<120;i++){
                        String  msg =  service.callB();

                    }
                }
            });
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    for (int i=0;i<120;i++){
                        String  msg =  service.callC();

                    }
                }
            });
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    @EnableDubbo(scanBasePackages = "com.lxr")
    @Configuration
    @PropertySource("classpath:/dubbo-consumer.properties")
    @ComponentScan(value = {"com.lxr"})
    static class ConsumerConfiguration {

    }

}
