package cn.com.lxr.service;

/**
 * @author lvxinran
 * @date 2020/7/20
 * @discribe
 */
public interface CodeService {

    String createCode(String email);

    int validateCode(String email,String code);
}
