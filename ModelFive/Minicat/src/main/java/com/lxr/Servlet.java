package com.lxr;

import java.io.IOException;

public interface Servlet {
    void init ();

    void destroy();

    void service(Request request, Response response) throws IOException;
}
