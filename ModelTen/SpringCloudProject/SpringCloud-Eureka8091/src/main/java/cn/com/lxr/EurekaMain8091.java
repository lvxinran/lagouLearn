package cn.com.lxr;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @author lvxinran
 * @date 2020/7/3
 * @discribe
 */
@SpringBootApplication
@EnableEurekaServer
public class EurekaMain8091 {
    public static void main(String[] args) {
        SpringApplication.run(EurekaMain8091.class,args);
    }
}
