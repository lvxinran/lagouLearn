package com.lagou.edu.factory;

import com.lagou.edu.annotation.MyAutowire;
import com.lagou.edu.annotation.MyComponent;
import com.lagou.edu.annotation.MyService;
import com.lagou.edu.annotation.MyTransactional;
import org.reflections.Reflections;
import org.reflections.scanners.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author 应癫
 *
 * 工厂类，生产对象（使用反射技术）
 */
public class BeanFactory {

    /**
     * 任务一：读取解析xml，通过反射技术实例化对象并且存储待用（map集合）
     * 任务二：对外提供获取实例对象的接口（根据id获取）
     */

    private static Map<String,Object> map = new HashMap<>();  // 存储对象


    // 任务二：对外提供获取实例对象的接口（根据id获取）
    public static  Object getBean(String id) {
        return map.get(id);
    }
    public static void init(){
        Reflections f = new Reflections("com.lagou.edu");
        Set<Class<?>> myServiceSet = f.getTypesAnnotatedWith(MyService.class);
        Set<Class<?>> myComponentSet = f.getTypesAnnotatedWith(MyComponent.class);
        myServiceSet.addAll(myComponentSet);
        for (Class<?> c : myServiceSet) {
            try {
                if(c.isInterface()){
                    continue;
                }
                Object bean = c.newInstance();

                Type[] genericSuperclass = c.getGenericInterfaces();
                if(genericSuperclass!=null&&genericSuperclass.length>0){
                    for (int i = 0; i < genericSuperclass.length; i++) {
                        map.put(genericSuperclass[i].getTypeName(), bean);
                    }
                }else{
                    map.put(c.getName(), bean);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        for(Map.Entry<String,Object> beanEntry:map.entrySet()){
            Object bean = beanEntry.getValue();
            //Transaction改装
            Class<?> clazz = bean.getClass();
            Method[] declaredMethods = clazz.getDeclaredMethods();
            if(declaredMethods!=null&&declaredMethods.length>0){
                for (Method method:declaredMethods){
                    Annotation[] declaredAnnotations = method.getDeclaredAnnotations();
                    if(declaredAnnotations!=null&&declaredAnnotations.length>0){
                        for(Annotation anno:declaredAnnotations){
                            if(anno instanceof MyTransactional){
                                ProxyFactory proxyFactory = (ProxyFactory)getBean("com.lagou.edu.factory.ProxyFactory");
                                map.put(beanEntry.getKey(),proxyFactory.getJdkProxy(bean));
                            }
                        }
                    }
                }
            }
            //field装配
            Field[] declaredFields = clazz.getDeclaredFields();
            for (Field field:declaredFields){
                Annotation[] declaredAnnotations = field.getDeclaredAnnotations();
                if(declaredAnnotations !=null){
                    for (Annotation annotation:declaredAnnotations){
                        if(annotation instanceof MyAutowire){
                            String name = field.getType().getName();
                            Object o = map.get(name);
                            try {
                                String methodName = "set" + field.getType().getSimpleName();
                                Method[] method = declaredMethods;
                                for (Method method1 : method) {
                                    if(method1.getName().equals(methodName)){
                                        method1.setAccessible(true);
                                        method1.invoke(bean,o);
                                    }
                                }
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            } catch (InvocationTargetException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }


        }
    }
}
