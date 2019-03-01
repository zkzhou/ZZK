package com.zkzhou.myframelib.uiframe.inject.manager;

import android.app.Activity;
import android.view.View;

import com.zkzhou.myframelib.uiframe.inject.annotation.ContentView;
import com.zkzhou.myframelib.uiframe.inject.annotation.EventBase;
import com.zkzhou.myframelib.uiframe.inject.annotation.InjectView;
import com.zkzhou.myframelib.uiframe.inject.invocation.ListenerInvocationHandler;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 注入管理类
 *
 * @author zhouzhankun
 * @time 19/3/1 09:07
 **/


public class InjectManager {

    /**
     * 初始化方法
     *
     * @param activity
     */
    public static void init(Activity activity) {
        injectLayout(activity);
        injectDeclaredViews(activity);
        injectEvents(activity);
    }

    /**
     * 事件注入
     *
     * @param activity
     */
    private static void injectEvents(Activity activity) {
        if (activity == null) {
            return;
        }
        //获取类
        Class<? extends Activity> clazz = activity.getClass();
        //获取方法的注解
        Method[] declaredMethods = clazz.getDeclaredMethods();
        if (declaredMethods == null || declaredMethods.length <= 0) {
            return;
        }
        for (Method method : declaredMethods) {
            Annotation[] annotations = method.getAnnotations();
            if (annotations == null || annotations.length <= 0) {
                continue;
            }
            for (Annotation annotation : annotations) {
                Class<? extends Annotation> annotationType = annotation.annotationType();
                if (annotationType != null) {
                    EventBase eventBase = annotationType.getAnnotation(EventBase.class);
                    if (eventBase != null) {
                        //获取3个函数值
                        String listenerSetter = eventBase.listenerSetter();
                        Class<?> listenerType = eventBase.listenerType();
                        String listenerCallback = eventBase.listenerCallback();

                        //动态代理
                        ListenerInvocationHandler listenerInvocationHandler = new ListenerInvocationHandler(activity);
                        listenerInvocationHandler.addMethod(listenerCallback,method);
                        Object proxy = Proxy.newProxyInstance(listenerType.getClassLoader(), new Class[]{listenerType}, listenerInvocationHandler);

                        try {
                            Method valueMethod = annotationType.getDeclaredMethod("values");
                            int[] viewIds = (int[]) valueMethod.invoke(annotation);
                            for (int viewId : viewIds) {
                                Method findViewById = clazz.getMethod("findViewById", int.class);
                                View view = (View) findViewById.invoke(activity, viewId);
                                Method setter = view.getClass().getMethod(listenerSetter, listenerType);
                                setter.invoke(view, proxy);
                            }
                        } catch (NoSuchMethodException e) {
                            e.printStackTrace();
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

    /**
     * 布局注入
     *
     * @param activity
     */
    private static void injectLayout(Activity activity) {
        if (activity == null) {
            return;
        }
        //获取类
        Class<? extends Activity> clazz = activity.getClass();
        //获取类注解
        ContentView annotation = clazz.getAnnotation(ContentView.class);
        if (annotation != null) {
            //获取注解的值
            int value = annotation.value();
            try {
                //找到setContentView方法
                Method method = clazz.getMethod("setContentView", int.class);
                //执行setContentView方法
                method.invoke(activity, value);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 控件注入
     *
     * @param activity
     */
    private static void injectDeclaredViews(Activity activity) {
        if (activity == null) {
            return;
        }
        //获取类
        Class<? extends Activity> clazz = activity.getClass();
        //获取该类中所有属性(public,protected,private,不包含基类)
        Field[] fields = clazz.getDeclaredFields();
        //遍历属性
        for (Field field : fields) {
            //获取属性的注解
            InjectView annotation = field.getAnnotation(InjectView.class);
            if (annotation != null) {
                //获取注解的值
                int value = annotation.value();
                try {
                    //找到findViewById方法
                    Method method = clazz.getMethod("findViewById", int.class);
                    //指定findViewById方法
                    View view = (View) method.invoke(activity, value);
                    //修改private私有权限
                    field.setAccessible(true);
                    //赋值给控件
                    field.set(activity, view);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 控件注入
     *
     * @param activity
     */
    private static void injectViews(Activity activity) {
        if (activity == null) {
            return;
        }
        //获取类
        Class<? extends Activity> clazz = activity.getClass();
        //获取该类以及基类中所有public属性
        Field[] fields = clazz.getFields();
        //遍历属性
        for (Field field : fields) {
            //获取属性的注解
            InjectView annotation = field.getAnnotation(InjectView.class);
            if (annotation != null) {
                //获取注解的值
                int value = annotation.value();
                try {
                    //找到findViewById方法
                    Method method = clazz.getMethod("findViewById", int.class);
                    //指定findViewById方法
                    View view = (View) method.invoke(activity, value);
                    //修改private私有权限
                    field.setAccessible(true);
                    //赋值给控件
                    field.set(activity, view);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
