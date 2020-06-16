package filter;

import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;

/**
 * @author lvxinran
 * @date 2020/6/16
 * @discribe
 */
@Activate(group = {CommonConstants.CONSUMER,CommonConstants.PROVIDER})
public class IpFilter implements Filter {
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        String ip_value = RpcContext.getContext().getAttachment("ip_value");
        System.out.println("服务获取ip"+ip_value);
        return  invoker.invoke(invocation);
    }
}
