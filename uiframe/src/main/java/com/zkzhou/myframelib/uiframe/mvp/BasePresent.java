package com.zkzhou.myframelib.uiframe.mvp;

import android.os.Bundle;

/**
 * @auther zhouzhankun
 * @time 18/9/17 15:01
 **/


public class BasePresent<V> implements IBasePresent {

    protected V mView;

    public void attachView(V view) {
        this.mView = view;
    }

    public void detachView() {
        this.mView = null;
    }

    public boolean isViewAttached() {
        return this.mView != null;
    }

    @Override
    public void onStart() {
        
    }

    @Override
    public void onDestroy() {
        detachView();
    }

    public void onSaveInstanceState(Bundle outState) {

    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {

    }

}
