package cn.com.lxr.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author lvxinran
 * @date 2020/7/28
 * @discribe
 */
@FeignClient(name = "CodeApplicaion")
public interface CodeFeignClient {

    @RequestMapping(value = "/api/code/validate",method = RequestMethod.POST)
    int validateCode(@RequestParam(value = "email")String email,@RequestParam(value = "code") String code);

}
