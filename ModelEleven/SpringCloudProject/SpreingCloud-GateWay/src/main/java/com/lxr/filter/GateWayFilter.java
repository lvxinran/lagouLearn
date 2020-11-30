package com.lxr.filter;

import com.lxr.client.UserFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.RequestPath;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lvxinran
 * @date 2020/7/28
 * @discribe
 */
@Component
public class GateWayFilter implements GlobalFilter {
    @Autowired
    private UserFeignClient userFeignClient;

    private Map<String,Integer> registerCount=new HashMap<>();
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        RequestPath path = request.getPath();
        String value = path.value();
        MultiValueMap<String, HttpCookie> cookies = request.getCookies();
        if(value.equals("/api/user/login")){
            return chain.filter(exchange);
        }else if(value.equals("/api/user/register") ){
            String hostName = request.getRemoteAddress().getAddress().getHostAddress();
            if(registerCount.getOrDefault(hostName,0)>3){
                response.setStatusCode(HttpStatus.SEE_OTHER);
                String data = "ip refused";
                DataBuffer wrap = response.bufferFactory().wrap(data.getBytes());
                return response.writeWith(Mono.just(wrap));
            }
            registerCount.put(hostName,registerCount.getOrDefault(hostName,0)+1);
            return chain.filter(exchange);
        }else if(value.equals("/api/code/create")){
            return chain.filter(exchange);

        }
        List<HttpCookie> tokenList = cookies.get("token");
        if(tokenList.size()!=1){
            return null;
        }else{
            String token = tokenList.get(0).getValue();
            String s = userFeignClient.validateToken(token);
            if(s.isEmpty()){
                response.setStatusCode(HttpStatus.NOT_MODIFIED);
                String data = "token wrong";
                DataBuffer wrap = response.bufferFactory().wrap(data.getBytes());
                return response.writeWith(Mono.just(wrap));
            }
        }
        return chain.filter(exchange);
    }
}
