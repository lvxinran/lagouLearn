package cn.com.lxr.service.impl;

import cn.com.lxr.bean.User;
import cn.com.lxr.client.CodeFeignClient;
import cn.com.lxr.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lvxinran
 * @date 2020/7/11
 * @discribe
 */
@Service
public class UserServiceImpl implements UserService {

    private Map<String, User> registerUser = new HashMap<>();

    @Autowired
    private CodeFeignClient codeFeignClient;

    @Override
    public int insertUser(String email, String password, String code) {
        if(registerUser.containsKey(email)){
            return -1;
        }
        int result = codeFeignClient.validateCode(email, code);
        if(result==0){
            //todo  code判断
            User user = new User(email,password);
            registerUser.put(email,user);
        }
        return result;
    }

    @Override
    public boolean getUserByEmail(String email) {
        return registerUser.containsKey(email);
    }

    @Override
    public String getUserByEmailAndPassword(String email, String password) {
        if(!registerUser.containsKey(email)){
            return "-1";//此邮箱没注册
        }
        User user = registerUser.get(email);
        if(!user.getPassword().equals(password)){
            return "-2";//密码不正确
        }
        return email;
    }

    @Override
    public String getEmailByToken(String token) {
        return "email";
    }
}
