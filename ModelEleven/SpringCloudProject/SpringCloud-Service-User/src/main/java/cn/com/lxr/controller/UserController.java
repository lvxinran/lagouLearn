package cn.com.lxr.controller;

import cn.com.lxr.bean.TokenEntity;
import cn.com.lxr.client.CodeFeignClient;
import cn.com.lxr.dao.TokenDao;
import cn.com.lxr.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.UUID;

/**
 * @author lvxinran
 * @date 2020/7/11
 * @discribe
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    UserService userService;


    @Autowired
    private TokenDao tokenDao;


    @PostMapping("/login")
    public String  login(@RequestParam("email") String email, @RequestParam("password") String password,HttpServletResponse response){
        TokenEntity tokenEntity = new TokenEntity();
        tokenEntity.setEmail(email);
        String token = UUID.randomUUID().toString();
        tokenEntity.setToken(token);
        tokenDao.save(tokenEntity);
        response.addCookie(new Cookie("token",token));
        return userService.getUserByEmailAndPassword(email, password);
    }
    @PostMapping("/register")
    public String register(@RequestParam("email") String email, @RequestParam("password") String password, @RequestParam("code")  String code){
        System.out.println("注册");
        int result = userService.insertUser(email, password, code);
        if(result==0){

        }
        return String.valueOf(result);
    }
    @GetMapping("/isRegistered/{email}")
    public boolean isRegistered(@PathVariable String email){
        return userService.getUserByEmail(email);
    }

    @GetMapping("/info/{token}")
    public String info(@PathVariable String token){
        return userService.getEmailByToken(token);
    }


}
