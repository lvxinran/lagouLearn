package com.lxr;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author lvxinran
 * @date 2020/4/29
 * @discribe
 */
public class StaticResourceUtil {

    /**
     * 读取输出文件，将静态资源输出
     * @param inputStream
     * @param outputStream
     * @throws IOException
     */
    public static void outputStaticResource(InputStream inputStream, OutputStream outputStream) throws IOException {
        int count = 0;
        while(count==0){
            count = inputStream.available();
        }
        int resourceSize = count;
        //输出响应头
        outputStream.write(HttpProtocolUtil.getHttpHeader200(resourceSize).getBytes());
        //输出具体内容
        long written = 0 ;
        //每次缓冲长度
        int byteSize = 1024;
        byte[] bytes = new byte[byteSize];
        while(written<resourceSize){
            if(written+byteSize>resourceSize){//最后一次处理，长度小于每次缓冲长度
                byteSize = (int) (resourceSize-written);//剩余长度
                bytes = new byte[byteSize];
            }
            inputStream.read(bytes);
            outputStream.write(bytes);
            outputStream.flush();
            written+=byteSize;
        }
        outputStream.close();
    }

    /**
     * 获取绝对路径
     * @param path
     * @return
     */
    public static String getAbsolutePath(String path){
        String absolutePath = StaticResourceUtil.class.getResource("/").getPath();
        return absolutePath.replaceAll("\\\\","/")+path;
    }
}
