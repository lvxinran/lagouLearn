package starter.controller;

import com.lxr.service.IpService;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.rpc.RpcContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


/**
 * @author lvxinran
 * @date 2020/6/16
 * @discribe
 */

@RestController
public class BaseController {

    @Reference(check = false ,loadbalance = "roundrobin")
    private IpService ipService;


    @RequestMapping("showIp")
    @ResponseBody
    public String showIp(HttpServletRequest request){

        RpcContext.getContext().setAttachment("ip_value", request.getRemoteAddr());

        ipService.showIp();

        return "success";
    }
}
