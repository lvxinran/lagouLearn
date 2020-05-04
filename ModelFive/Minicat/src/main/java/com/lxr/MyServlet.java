package com.lxr;

import java.io.IOException;

/**
 * @author lvxinran
 * @date 2020/4/29
 * @discribe
 */
public class MyServlet extends HttpServlet {
    @Override
    public void doGet(Request request, Response response) throws IOException {
        String content = "<h1>MyServlet get</h1>";
        response.outPut(HttpProtocolUtil.getHttpHeader200(content.getBytes().length)+content);
    }

    @Override
    public void doPost(Request request, Response response) throws IOException {

        String content = "<h1>MyServlet post</h1>";
        response.outPut(HttpProtocolUtil.getHttpHeader200(content.getBytes().length)+content);
    }

    @Override
    public void init() {

    }

    @Override
    public void destroy() {

    }
}
