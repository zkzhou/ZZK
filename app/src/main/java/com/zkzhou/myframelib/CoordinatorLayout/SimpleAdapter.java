package com.zkzhou.myframelib.CoordinatorLayout;

import android.content.Context;
import android.view.View;

import com.zkzhou.myframelib.R;
import com.zkzhou.myframelib.uiframe.adapter.AbsRecyclerBaseAdapter;

import java.util.List;

/**
 * @auther zhouzhankun
 * @time 18/9/29 10:10
 **/


public class SimpleAdapter extends AbsRecyclerBaseAdapter<String,SimpleAdapterHelper> {

    /**
     * Create a QuickAdapter.
     *
     * @param context     The context.
     * @param layoutResId The layout resource id of each item.
     */
    public SimpleAdapter(Context context, int layoutResId) {
        super(context, layoutResId);
    }

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param context     The context.
     * @param layoutResId The layout resource id of each item.
     * @param data        A new list is created out of this one to avoid mutable list
     */
    public SimpleAdapter(Context context, int layoutResId, List<String> data) {
        super(context, layoutResId, data);
    }

    /**
     * Implement this method and use the helper to adapt the view to the given item.
     *
     * @param helper A fully initialized helper.
     * @param item   The item that needs to be displayed.
     */
    @Override
    protected void convert(SimpleAdapterHelper helper, String item) {
        helper.getTextView(R.id.textview).setText(item);
    }

    /**
     * Implement this method and make a view holder.
     *
     * @param view Target view
     * @return
     */
    @Override
    protected SimpleAdapterHelper makeViewHolder(View view) {
        return new SimpleAdapterHelper(view);
    }
}
