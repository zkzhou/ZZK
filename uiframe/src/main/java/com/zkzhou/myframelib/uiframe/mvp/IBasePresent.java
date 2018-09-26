package com.zkzhou.myframelib.uiframe.mvp;

import android.app.Activity;

/**
 * @auther zhouzhankun
 * @time 18/9/17 15:08
 **/


public interface IBasePresent {

    /**
     * 或一些初始化的操作
     */
    void onStart();

    /**
     * 在框架中 {@link Activity#onDestroy()} 会默认调用{@link IBasePresent#onDestroy()}
     */
    void onDestroy();

}
