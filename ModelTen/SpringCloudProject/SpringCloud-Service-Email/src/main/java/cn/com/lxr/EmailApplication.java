package cn.com.lxr;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @author lvxinran
 * @date 2020/7/3
 * @discribe
 */
@SpringBootApplication
@EnableEurekaClient
public class EmailApplication {
    public static void main(String[] args) {
        SpringApplication.run(EmailApplication.class,args);
    }
}
