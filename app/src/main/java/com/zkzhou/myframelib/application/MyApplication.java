package com.zkzhou.myframelib.application;

import android.app.Application;
import android.os.Build;

import com.alibaba.android.arouter.launcher.ARouter;
import com.zkzhou.myframelib.BuildConfig;

/**
 * @author zhouzhankun
 * @time 19/3/4 14:32
 **/


public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            ARouter.openLog();
            ARouter.openDebug();
        }
        ARouter.init(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        ARouter.getInstance().destroy();
    }
}
