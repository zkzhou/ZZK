package com.zkzhou.myframelib.uiframe.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public abstract class AbsRecyclerBaseAdapter<T, H extends BaseViewHolder> extends RecyclerView.Adapter<H> implements View.OnClickListener{

    protected static final String TAG = AbsRecyclerBaseAdapter.class.getSimpleName();

    protected final Context context;

    protected final int layoutResId;

    protected final List<T> data;

    private OnItemClickListener mOnItemClickListener = null;

    interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    /**
     * Create a QuickAdapter.
     * @param context     The context.
     * @param layoutResId The layout resource id of each item.
     */
    public AbsRecyclerBaseAdapter(Context context, int layoutResId) {
        this(context, layoutResId, null);
    }

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     * @param context     The context.
     * @param layoutResId The layout resource id of each item.
     * @param data        A new list is created out of this one to avoid mutable list
     */
    public AbsRecyclerBaseAdapter(Context context, int layoutResId, List<T> data) {
        this.data = data == null ? new ArrayList<T>() : data;
        this.context = context;
        this.layoutResId = layoutResId;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public T getItem(int position) {
        if (position >= data.size()) return null;
        return data.get(position);
    }
    @Override
    public H onCreateViewHolder(ViewGroup viewGroup,  int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(layoutResId, viewGroup, false);
        view.setOnClickListener(this);
        return makeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(H helper,  int position) {
        helper.itemView.setTag(position);
        T item = getItem(position);
        convert(helper, item);
    }

    /**
     * Implement this method and use the helper to adapt the view to the given item.
     * @param helper A fully initialized helper.
     * @param item   The item that needs to be displayed.
     */
    protected abstract void convert(H helper, T item);

    /**
     * Implement this method and make a view holder.
     * @param view Target view
     * @return
     */
    protected abstract H makeViewHolder(View view);

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(v,(int)v.getTag());
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

}
