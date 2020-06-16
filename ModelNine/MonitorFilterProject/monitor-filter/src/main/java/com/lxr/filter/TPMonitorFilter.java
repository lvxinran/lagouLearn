package com.lxr.filter;

import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author lvxinran
 * @date 2020/6/17
 * @discribe
 */
@Activate(group = CommonConstants.CONSUMER)
public class TPMonitorFilter implements Filter {

    private static Map<String, List<Integer>> timeCalSession1 = new ConcurrentHashMap<>();

    private static Map<String, List<Integer>[]> timeCalSession2 = new ConcurrentHashMap<>();

    private int size = 12;

    private static int index = 0;

    public TPMonitorFilter() {
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(this::outTime,5,5, TimeUnit.SECONDS);
    }

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        long startTime = System.currentTimeMillis();

        Result result = invoker.invoke(invocation);
        long endTime = System.currentTimeMillis();
        String methodName = invocation.getMethodName();
        timeCalSession1.computeIfAbsent(methodName,n->new ArrayList<>()).add((int)(endTime-startTime));
        return result;
    }
    private void outTime(){
        for(Map.Entry<String, List<Integer>> entry:timeCalSession1.entrySet()){
            List<Integer> timeList = entry.getValue();
            if(timeCalSession2.get(entry.getKey())==null){
                timeCalSession2.put(entry.getKey(),new List[size]);
            }
            timeCalSession2.get(entry.getKey())[index] = timeList;
            List<Integer>[] timeArray = timeCalSession2.get(entry.getKey());
            List<Integer> allTime = new ArrayList<>();
            for(List array:timeArray){
                if(array==null){
                    continue;
                }
                allTime.addAll(array);
            }
            List<Integer> collect = allTime.stream().sorted().collect(Collectors.toList());
            long ninetyNine = collect.size() * 99L / 100;
            long ninety = collect.size() * 90L / 100;
            System.out.println("1分钟内请求数量为："+collect.size());
            System.out.println("方法"+entry.getKey()+"的TP90为:"+collect.get((int)ninety)+",TP99为:"+collect.get((int)ninetyNine));

        }
        timeCalSession1 = new ConcurrentHashMap<>();
        index++;
        if(index==12) {
            index = 0;
        }
    }
}
