package com.zkzhou.myframelib.CoordinatorLayout;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.zkzhou.myframelib.R;
import com.zkzhou.myframelib.uiframe.mvp.ImmersiveStatusBarActivity;

import java.util.ArrayList;
import java.util.List;

import myframelib.zkzhou.com.common.app.ViewHolderUtil;

/**
 * @auther zhouzhankun
 * @time 18/9/29 09:43
 **/


public class SimpleCoordinatorLayoutActivity extends ImmersiveStatusBarActivity<SimplePresenter> {

    private CoordinatorLayout coordinatorLayout;
    private RecyclerView recyclerView;
    private SimpleAdapter simpleAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * 显示加载视图
     */
    @Override
    public void showLoadingView() {

    }

    /**
     * 显示内容视图
     */
    @Override
    public void showContentView() {

    }

    /**
     * 显示空视图
     */
    @Override
    public void showEmptyView() {

    }

    /**
     * 显示刷新视图
     */
    @Override
    public void showRefreshView() {

    }

    /**
     * 显示出错视图
     */
    @Override
    public void showErrorView() {

    }

    /**
     * 上下文
     *
     * @return
     */
    @Override
    public Context getContext() {
        return this;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_simple_coordinator_layout;
    }

    @Override
    public void setupView() {
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator_layout);
        recyclerView = ViewHolderUtil.get(coordinatorLayout, R.id.recycler_view);
    }

    @Override
    public void createPresent() {
        mPresent = new SimplePresenter();
    }

    @Override
    public void initData() {
        List<String>list = new ArrayList<>();
        for (int i=0; i<100; i++) {
            list.add("我是第" + i + "个item");
        }
        simpleAdapter = new SimpleAdapter(this,R.layout.simple_adapter_item,list);
        recyclerView.setAdapter(simpleAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean hasImmersive() {
        return false;
    }
}
