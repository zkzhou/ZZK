package com.zkzhou.myframelib.uiframe;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * @auther zhouzhankun
 * @time 18/9/17 15:17
 **/


public abstract class AbsBaseActivity<P extends BasePresent> extends Activity implements IBaseView {

    protected P mPresent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        setupView();
        initPresent();
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

    protected abstract int getContentView();

    public abstract void setupView();

    public abstract void createPresent();

    public abstract void initData();
}
