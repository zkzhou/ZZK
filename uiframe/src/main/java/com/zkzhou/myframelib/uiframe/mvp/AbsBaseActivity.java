package com.zkzhou.myframelib.uiframe.mvp;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.alibaba.android.arouter.launcher.ARouter;
import com.zkzhou.myframelib.uiframe.inject.manager.InjectManager;

/**
 * @auther zhouzhankun
 * @time 18/9/17 15:17
 **/


public abstract class AbsBaseActivity<P extends BasePresent> extends Activity implements IBaseView {

    protected P mPresent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //自定义注入
        InjectManager.init(this);
        setupView();
        initPresent();
        //ARouter注入
        ARouter.getInstance().inject(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mPresent.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mPresent.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresent.onDestroy();
    }

    private void initPresent() {
        if (mPresent == null) {
            createPresent();
        }
        if (!mPresent.isViewAttached()) {
            mPresent.attachView(this);
        }
        mPresent.onStart();
        initData();
    }

    public abstract void setupView();

    public abstract void createPresent();

    public abstract void initData();
}
