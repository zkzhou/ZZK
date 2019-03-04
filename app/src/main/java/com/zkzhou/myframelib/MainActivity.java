package com.zkzhou.myframelib;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.zkzhou.myframelib.CoordinatorLayout.SimpleCoordinatorLayoutActivity;
import com.zkzhou.myframelib.uiframe.inject.annotation.ContentView;
import com.zkzhou.myframelib.uiframe.inject.annotation.InjectView;
import com.zkzhou.myframelib.uiframe.inject.annotation.OnClick;
import com.zkzhou.myframelib.uiframe.inject.annotation.OnLongClick;
import com.zkzhou.myframelib.uiframe.mvp.ImmersiveStatusBarActivity;
import com.zkzhou.myframelib.util.Constants;

@Route(path = Constants.ACTIVITY_URL_MAIN)
@ContentView(R.layout.activity_main)
public class MainActivity extends ImmersiveStatusBarActivity<MainPresent> {

    @InjectView(R.id.tv)
    private TextView tv;
    @InjectView(R.id.btn)
    private Button btn;
    @InjectView(R.id.btn_jump)
    private Button jumpBtn;

    @Override
    public void setupView() {

    }

    @OnClick(values = {R.id.btn, R.id.tv,R.id.btn_jump})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn:
                Toast.makeText(MainActivity.this,"btn click",Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv:
                Toast.makeText(MainActivity.this,"tv click",Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_jump:
//                startActivity(new Intent(this, SimpleCoordinatorLayoutActivity.class));
                ARouter.getInstance().build(Constants.ACTIVITY_URL_SIMPLECOORDINATORLAYOUTACTIVITY).navigation();
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
