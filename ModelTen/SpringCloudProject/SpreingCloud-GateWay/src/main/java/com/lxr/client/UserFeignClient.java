package com.lxr.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * @author lvxinran
 * @date 2020/7/28
 * @discribe
 */
@FeignClient(name = "UserApplication")
public interface UserFeignClient {

    @GetMapping("/info/{token}")
    String validateToken(@PathVariable("token") String token);

}