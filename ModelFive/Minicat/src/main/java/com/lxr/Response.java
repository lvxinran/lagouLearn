package com.lxr;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author lvxinran
 * @date 2020/4/29
 * @discribe
 */
public class Response {
    private OutputStream outputStream;

    public OutputStream getOutputStream() {
        return outputStream;
    }

    public void setOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }
    Response(OutputStream outputStream){
        this.outputStream = outputStream;
    }
    Response(){}

    /**
     * 输出流输出指定字符串
     * @param content
     */
    public void outPut(String content) throws IOException {
        outputStream.write(content.getBytes());
    }


    /**
     * 根据url 输出静态资源，先根据path获取绝对路径，读取绝对路径文件，输出资源
     * @param path url
     */
    public void outputHtml(String path) throws IOException {
        String absoluteResourcePath = StaticResourceUtil.getAbsolutePath(path);

        File file = new File(absoluteResourcePath);
        if(file.exists() && file.isFile()){
            //输出静态文件
            StaticResourceUtil.outputStaticResource(new FileInputStream(file),outputStream);
        }else{
            //输出404
            outPut(HttpProtocolUtil.getHttpHeader404());
        }
    }
}
