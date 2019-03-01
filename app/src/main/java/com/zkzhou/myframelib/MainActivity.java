package com.zkzhou.myframelib;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.zkzhou.myframelib.uiframe.inject.annotation.ContentView;
import com.zkzhou.myframelib.uiframe.inject.annotation.OnClick;
import com.zkzhou.myframelib.uiframe.inject.manager.InjectManager;
import com.zkzhou.myframelib.uiframe.inject.annotation.InjectView;
import com.zkzhou.myframelib.uiframe.inject.annotation.OnLongClick;
import com.zkzhou.myframelib.uiframe.mvp.ImmersiveStatusBarActivity;

@ContentView(R.layout.activity_main)
public class MainActivity extends ImmersiveStatusBarActivity<MainPresent> {

    @InjectView(R.id.tv)
    private TextView tv;
    @InjectView(R.id.btn)
    private Button btn;

    @Override
    public void setupView() {
        InjectManager.init(this);
    }

    @OnClick(values = {R.id.btn, R.id.tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn:
                Toast.makeText(MainActivity.this,"btn click",Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv:
                Toast.makeText(MainActivity.this,"tv click",Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @OnLongClick(values = {R.id.btn,R.id.tv})
    private boolean onLongClick(View view) {
        switch (view.getId()) {
            case R.id.btn:
                Toast.makeText(MainActivity.this,"btn longClick",Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv:
                Toast.makeText(MainActivity.this,"tv longClick",Toast.LENGTH_SHORT).show();
                break;
        }
        return false;
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
