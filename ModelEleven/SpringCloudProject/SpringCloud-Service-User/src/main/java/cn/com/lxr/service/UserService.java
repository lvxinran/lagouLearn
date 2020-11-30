package cn.com.lxr.service;

public interface UserService {

    int insertUser(String email,String password,String code);

    boolean getUserByEmail(String email);

    String getUserByEmailAndPassword(String email,String password);

    String getEmailByToken(String token);
}
