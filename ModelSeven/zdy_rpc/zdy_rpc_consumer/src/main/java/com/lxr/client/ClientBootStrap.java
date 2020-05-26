package com.lxr.client;

import com.lxr.coder.RpcRequest;
import com.lxr.service.UserService;

/**
 * @author lvxinran
 * @date 2020/5/25
 * @discribe
 */
public class ClientBootStrap {
    public static RpcRequest request = new RpcRequest();

    public static void main(String[] args) throws InterruptedException {

        RpcConsumer consumer = new RpcConsumer();
        UserService service = (UserService) consumer.createProxy(UserService.class, request);
        for(;;){
            Thread.sleep(1000);
            System.out.println(service.sayHello("are you ok?"));
        }
    }

}
