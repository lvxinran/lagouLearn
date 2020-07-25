package cn.com.lxr.controller;

import cn.com.lxr.service.CodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author lvxinran
 * @date 2020/7/20
 * @discribe
 */

@RestController
@RequestMapping("/api/code")
public class CodeController {

    @Autowired
    private CodeService codeService;


    @GetMapping("/create")
    public String createCode(@RequestParam(value = "email") String email){
        String code = codeService.createCode(email);
        System.out.println("生成验证码"+code);
        return code;
    }

    @PostMapping("/validate")
    public int validateCode(@RequestParam(value = "email") String email,@RequestParam(value = "code") String code){
        return codeService.validateCode(email,code);
    }

}
