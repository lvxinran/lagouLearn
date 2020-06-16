package starter;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author lvxinran
 * @date 2020/6/16
 * @discribe
 */

@SpringBootApplication
public class IpAppMain {

    public static void main(String[] args) {
        SpringApplication.run(IpAppMain.class,args);
    }
    @EnableDubbo(scanBasePackages = "starter")
    @Configuration
    @PropertySource("classpath:/dubbo.properties")
    @ComponentScan(value = {"starter"})
    static class ConsumerConfiguration {

    }
}
