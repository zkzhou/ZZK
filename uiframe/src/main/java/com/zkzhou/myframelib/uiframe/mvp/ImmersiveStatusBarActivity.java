package com.zkzhou.myframelib.uiframe.mvp;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.zkzhou.myframelib.uiframe.AbsBaseActivity;
import com.zkzhou.myframelib.uiframe.BasePresent;

import myframelib.zkzhou.com.common.ImmersiveStatusBarUtil;

/**
 * @auther zhouzhankun
 * @time 18/9/26 15:34
 **/


public abstract class ImmersiveStatusBarActivity<P extends BasePresent> extends AbsBaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (hasImmersive()) {
            setImmersiveStatusBar();
        }
    }

    /**
     * 可重写该方法适配自定义的沉浸式
     */
    protected void setImmersiveStatusBar() {
        ImmersiveStatusBarUtil.setTranslucent(this);
    }

    public abstract boolean hasImmersive();
}
