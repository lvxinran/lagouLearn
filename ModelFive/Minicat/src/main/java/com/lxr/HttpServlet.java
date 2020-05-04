package com.lxr;

import java.io.IOException;

/**
 * @author lvxinran
 * @date 2020/4/29
 * @discribe
 */
public abstract class HttpServlet implements Servlet{

    public abstract void doGet(Request request,Response response) throws IOException;

    public abstract void doPost(Request request,Response response) throws IOException;

    @Override
    public void service(Request request, Response response) throws IOException {
        try {
            Thread.sleep(100000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if("GET".equalsIgnoreCase(request.getMethod())){
            doGet(request,response);
        }else{
            doPost(request,response);
        }
    }
}
