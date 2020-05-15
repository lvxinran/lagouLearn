package com.lxr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * @author lvxinran
 * @date 2020/5/14
 * @discribe
 */
@SpringBootApplication
@EnableCaching
@EnableRedisHttpSession
public class AppStart extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(AppStart.class);
    }
}
