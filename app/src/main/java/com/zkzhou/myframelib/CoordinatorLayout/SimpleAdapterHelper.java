package com.zkzhou.myframelib.CoordinatorLayout;

import android.view.View;
import android.widget.TextView;

import com.zkzhou.myframelib.uiframe.adapter.BaseViewHolder;

/**
 * @auther zhouzhankun
 * @time 18/9/29 14:17
 **/


public class SimpleAdapterHelper extends BaseViewHolder {

    public SimpleAdapterHelper(View itemView) {
        super(itemView);
    }

    public TextView getTextView(int viewId) {
        return retrieveView(viewId);
    }
}
