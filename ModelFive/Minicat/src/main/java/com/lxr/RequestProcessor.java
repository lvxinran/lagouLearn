package com.lxr;

import com.lxr.classLoader.MyClassLoader;
import com.lxr.core.Mapper;
import com.lxr.core.Wrapper;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.Socket;
import java.net.URL;
import java.util.Map;

/**
 * @author lvxinran
 * @date 2020/4/29
 * @discribe
 */
public class RequestProcessor extends Thread {
    private Socket socket;
//    private Map<String,HttpServlet> servletMap;
    private Mapper mapper;
    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }


    RequestProcessor(Socket socket , Mapper mapper){
        this.socket = socket;
        this.mapper = mapper;
    }
    @Override
    public void run() {
        try{
            InputStream inputStream = socket.getInputStream();
            Request request = new Request(inputStream);
            Response response = new Response(socket.getOutputStream());
            String url = request.getUrl();

            Wrapper wrapper = mapper.getServletByURL(url);
            if(wrapper==null){
                response.outputHtml(url);
            }else{
//                servletMap.get(url).service(request,response);
                URL loaderURL = new File(wrapper.getBasePath()+"\\"+wrapper.getContext().getName()+"\\"+"classes").toURI().toURL();
                MyClassLoader classLoader = new MyClassLoader(loaderURL);
                Class<?> clazz= classLoader.loadClass(wrapper.getServlet());
                Method[] declaredMethods = clazz.getDeclaredMethods();
                for(Method method:declaredMethods){
                    if(method.getName().equals("doGet")){
                        method.setAccessible(true);
                        method.invoke(clazz.newInstance(),null,null);
                        break;
                    }
                }
                String content = "<h1>MyServlet get</h1>";
                response.outPut(HttpProtocolUtil.getHttpHeader200(content.getBytes().length)+content);
            }
            socket.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
