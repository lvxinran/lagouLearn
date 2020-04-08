package com.lagou.edu.servlet;

import com.lagou.edu.annotation.MyAutowire;
import com.lagou.edu.annotation.MyComponent;
import com.lagou.edu.factory.BeanFactory;
import com.lagou.edu.factory.ProxyFactory;
import com.lagou.edu.service.impl.TransferServiceImpl;
import com.lagou.edu.utils.JsonUtils;
import com.lagou.edu.pojo.Result;
import com.lagou.edu.service.TransferService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author 应癫
 */
@WebServlet(name="transferServlet",urlPatterns = "/transferServlet")
@MyComponent
public class TransferServlet extends HttpServlet {


    static {
        BeanFactory.init();
    }
    private TransferService transferService = (TransferService)BeanFactory.getBean("com.lagou.edu.service.TransferService") ;

    public void setTransferService(TransferService transferService) {
        this.transferService = transferService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // 设置请求体的字符编码
        req.setCharacterEncoding("UTF-8");

        String fromCardNo = req.getParameter("fromCardNo");
        String toCardNo = req.getParameter("toCardNo");
        String moneyStr = req.getParameter("money");
        int money = Integer.parseInt(moneyStr);

        Result result = new Result();

        try {

            // 2. 调用service层方法
            transferService.transfer(fromCardNo,toCardNo,money);
            result.setStatus("200");
        } catch (Exception e) {
            e.printStackTrace();
            result.setStatus("201");
            result.setMessage(e.toString());
        }

        // 响应
        resp.setContentType("application/json;charset=utf-8");
        resp.getWriter().print(JsonUtils.object2Json(result));
    }

    @Override
    public void init() {
        BeanFactory.init();
    }
}
