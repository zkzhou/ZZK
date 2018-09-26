package com.zkzhou.myframelib;

import android.content.Context;

import com.zkzhou.myframelib.uiframe.mvp.ImmersiveStatusBarActivity;

public class MainActivity extends ImmersiveStatusBarActivity<MainPresent> {

    @Override
    public int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    public void setupView() {

    }

    @Override
    public void createPresent() {
        mPresent = new MainPresent();
    }

    @Override
    public void initData() {

    }

    @Override
    public void showLoadingView() {

    }

    @Override
    public void showContentView() {

    }

    @Override
    public void showEmptyView() {

    }

    @Override
    public void showRefreshView() {

    }

    @Override
    public void showErrorView() {

    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public boolean hasImmersive() {
        return true;
    }
}
