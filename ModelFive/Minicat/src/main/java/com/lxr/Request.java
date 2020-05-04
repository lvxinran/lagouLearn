package com.lxr;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author lvxinran
 * @date 2020/4/29
 * @discribe 封装请求信息
 */
public class Request {
    private String method;
    private String url;
    private InputStream inputStream;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    Request(InputStream inputStream) throws IOException {
        this.inputStream = inputStream;
        int count = 0;
        while(count==0){
            count = inputStream.available();
        }
        byte[] bytes = new byte[count];
        inputStream.read(bytes);
        String inputStr = new String(bytes);
        //请求头第一行信息
        String firstLine = inputStr.split("\\n")[0];
        String[] infos = firstLine.split(" ");
        this.method = infos[0];
        this.url = infos[1];
        System.out.println("======>method:"+method);
        System.out.println("======>url:"+url);
    }
    Request(){
    }
}
