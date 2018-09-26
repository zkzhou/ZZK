package com.zkzhou.myframelib.uiframe.mvp;

import android.content.Context;

/**
 * @auther zhouzhankun
 * @time 18/9/17 15:13
 **/


public interface IBaseView {

    /**
     * 显示加载视图
     */
    void showLoadingView();

    /**
     * 显示内容视图
     */
    void showContentView();

    /**
     * 显示空视图
     */
    void showEmptyView();

    /**
     * 显示刷新视图
     */
    void showRefreshView();

    /**
     * 显示出错视图
     */
    void showErrorView();

    /**
     * 上下文
     * @return
     */
    Context getContext();
}
