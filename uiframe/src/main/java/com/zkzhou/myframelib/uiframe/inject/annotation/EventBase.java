package com.zkzhou.myframelib.uiframe.inject.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author zhouzhankun
 * @time 19/3/1 10:46
 **/

@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface EventBase {
    String listenerSetter();

    Class<?> listenerType();

    String listenerCallback();
}
