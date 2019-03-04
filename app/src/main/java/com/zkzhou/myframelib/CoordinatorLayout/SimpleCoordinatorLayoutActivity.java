package com.zkzhou.myframelib.CoordinatorLayout;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.zkzhou.myframelib.R;
import com.zkzhou.myframelib.uiframe.adapter.AbsRecyclerBaseAdapter;
import com.zkzhou.myframelib.uiframe.inject.annotation.ContentView;
import com.zkzhou.myframelib.uiframe.inject.annotation.InjectView;
import com.zkzhou.myframelib.uiframe.mvp.ImmersiveStatusBarActivity;
import com.zkzhou.myframelib.util.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * @auther zhouzhankun
 * @time 18/9/29 09:43
 **/

@Route(path = Constants.ACTIVITY_URL_SIMPLECOORDINATORLAYOUTACTIVITY)
@ContentView(R.layout.activity_simple_coordinator_layout)
public class SimpleCoordinatorLayoutActivity extends ImmersiveStatusBarActivity<SimplePresenter> {

    @InjectView(R.id.coordinator_layout)
    private CoordinatorLayout coordinatorLayout;
    @InjectView(R.id.recycler_view)
    private RecyclerView recyclerView;

    private SimpleAdapter simpleAdapter;
    private View headerView, footerView;

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
    public void setupView() {
        headerView = LayoutInflater.from(getContext()).inflate(R.layout.recycler_header_view, null);
        footerView = LayoutInflater.from(getContext()).inflate(R.layout.recycler_footer_view, null);
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
        simpleAdapter.addHeaderView(headerView);
        simpleAdapter.addFooterView(footerView);
        simpleAdapter.setOnItemClickListener(new AbsRecyclerBaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(getContext(),"我是第"+position+"个item",Toast.LENGTH_LONG).show();
            }
        });
        recyclerView.setAdapter(simpleAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean hasImmersive() {
        return false;
    }
}
