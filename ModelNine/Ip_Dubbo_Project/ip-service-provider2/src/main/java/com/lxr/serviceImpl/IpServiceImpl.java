package com.lxr.serviceImpl;


import com.lxr.service.IpService;
import org.apache.dubbo.config.annotation.Service;

/**
 * @author lvxinran
 * @date 2020/6/16
 * @discribe
 */
@Service
public class IpServiceImpl implements IpService {
    @Override
    public String showIp() {
        System.out.println("A服务");
        return "A服务";
    }
}
