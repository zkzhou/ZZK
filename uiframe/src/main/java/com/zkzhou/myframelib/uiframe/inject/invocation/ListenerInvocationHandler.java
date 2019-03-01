package com.zkzhou.myframelib.uiframe.inject.invocation;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * @author zhouzhankun
 * @time 19/3/1 14:36
 **/


public class ListenerInvocationHandler implements InvocationHandler {


    private Object target;
    private HashMap<String, Method> methodHashMap;

    public ListenerInvocationHandler(Object target) {
        this.target = target;
        this.methodHashMap = new HashMap<>();
    }

    public void addMethod(String methodName, Method method) {
        this.methodHashMap.put(methodName, method);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (target != null) {
            String methodName = method.getName();
            method = methodHashMap.get(methodName);
            if (method != null) {
                method.setAccessible(true);
                return method.invoke(target,args);
            }
        }
        return null;
    }
}
