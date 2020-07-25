package cn.com.lxr.service.impl;

import cn.com.lxr.dao.CodeDao;
import cn.com.lxr.entity.AuthCode;
import cn.com.lxr.service.CodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;

/**
 * @author lvxinran
 * @date 2020/7/20
 * @discribe
 */
@Service
public class CodeServiceImpl implements CodeService {
    @Autowired
    private CodeDao codeDao;


    @Override
    public String createCode(String email) {
        AuthCode authCode = new AuthCode();
        Calendar instance = Calendar.getInstance();
        authCode.setCreatetime(instance.getTime());
        authCode.setEmail(email);
        instance.add(Calendar.MINUTE,10);
        authCode.setExpiretime(instance.getTime());
        //生成6位
        StringBuilder builder = new StringBuilder();
        for(int i = 0;i<6;i++){
            builder.append(Double.valueOf(Math.random()*10).intValue());
        }
        authCode.setCode(builder.toString());
        codeDao.save(authCode);
        return builder.toString();
    }

    @Override
    public int validateCode(String email, String code) {
        if(code.length()!=6){
            return 1;
        }
        AuthCode authCode = new AuthCode();
        authCode.setEmail(email);
        ExampleMatcher matcher = ExampleMatcher.matching().withIgnorePaths("id");
        List<AuthCode> all = codeDao.findAll(Example.of(authCode,matcher));

        int laterCodeTime = 0;
        String laterCode = "";
        long endTime = 0;
        for(AuthCode oneCode:all){
            if(oneCode.getCreatetime().getTime()/1000<laterCodeTime){
                continue;
            }
            laterCodeTime = (int)(oneCode.getCreatetime().getTime()/1000);
            laterCode = oneCode.getCode();
            endTime = oneCode.getExpiretime().getTime();
        }
        if(laterCode.isEmpty()||!laterCode.equals(code)){
            return 1;//验证码错误
        }
        if(endTime!=0&&endTime<System.currentTimeMillis()){
            return 2;//过期
        }

        return 0;//正确
    }
}
