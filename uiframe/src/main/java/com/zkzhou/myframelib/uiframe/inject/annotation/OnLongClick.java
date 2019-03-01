package com.zkzhou.myframelib.uiframe.inject.annotation;

import android.view.View;

import com.zkzhou.myframelib.uiframe.inject.annotation.EventBase;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author zhouzhankun
 * @time 19/3/1 11:11
 **/

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@EventBase(listenerSetter = "setOnLongClickListener", listenerType = View.OnLongClickListener.class , listenerCallback = "onLongClick")
public @interface OnLongClick {
    int[] values();
}
