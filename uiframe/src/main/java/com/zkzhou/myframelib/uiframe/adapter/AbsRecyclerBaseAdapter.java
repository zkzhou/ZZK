package com.zkzhou.myframelib.uiframe.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

public abstract class AbsRecyclerBaseAdapter<T, H extends BaseViewHolder> extends RecyclerView.Adapter<H> implements View.OnClickListener {

    protected static final String TAG = AbsRecyclerBaseAdapter.class.getSimpleName();

    private static final int MODE_HEADER = 0;          //带头部模式
    private static final int MODE_FOOTER = 1;          //带尾部模式
    private static final int MODE_NORMAL = 2;          //正常模式

    private static final int HEADER_VIEW_COUNT = 1;    //头部数量
    private static final int FOOTER_VIEW_COUNT = 1;    //尾部数量

    protected final Context context;
    private OnItemClickListener mOnItemClickListener = null;
    protected final int layoutResId;
    protected FrameLayout headerFly, footerFly;
    protected final List<T> data;

    /**
     * Create a QuickAdapter.
     *
     * @param context     The context.
     * @param layoutResId The layout resource id of each item.
     */
    public AbsRecyclerBaseAdapter(Context context, int layoutResId) {
        this(context, layoutResId, null);
    }

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param context     The context.
     * @param layoutResId The layout resource id of each item.
     * @param data        A new list is created out of this one to avoid mutable list
     */
    public AbsRecyclerBaseAdapter(Context context, int layoutResId, List<T> data) {
        this.data = data == null ? new ArrayList<T>() : data;
        this.context = context;
        this.layoutResId = layoutResId;
    }

    public void addHeaderView(View headerView) {
        if (headerView == null) {
            return;
        }

        if (headerFly == null) {
            headerFly = new FrameLayout(context);
            headerFly.setLayoutParams(new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));
        } else {
            headerFly.removeAllViews();
        }
        headerFly.addView(headerView);
        notifyItemInserted(0);
    }

    public View getHeaderLayout() {
        return this.headerFly;
    }

    public void addFooterView(View footerView) {

        if (footerView == null) {
            return;
        }

        if (footerFly == null) {
            footerFly = new FrameLayout(context);
            footerFly.setLayoutParams(new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));
        } else {
            headerFly.removeAllViews();
        }
        footerFly.addView(footerView);
        notifyItemInserted(getItemCount() - HEADER_VIEW_COUNT);
    }

    public View getFooterLayout() {
        return this.footerFly;
    }

    @Override
    public int getItemViewType(int position) {
        if (headerFly == null && footerFly == null) {
            return MODE_NORMAL;
        } else if (headerFly != null && footerFly != null) {
            if (position == 0) {
                return MODE_HEADER;
            }
            if (position == getItemCount() - HEADER_VIEW_COUNT) {
                return MODE_FOOTER;
            }
            return MODE_NORMAL;
        } else if (headerFly != null && footerFly == null) {
            if (position == 0) {
                return MODE_HEADER;
            }
            return MODE_NORMAL;
        } else {
            if (position == getItemCount() - HEADER_VIEW_COUNT) {
                return MODE_FOOTER;
            }
            return MODE_NORMAL;
        }
    }

    @Override
    public int getItemCount() {
        if (headerFly == null && footerFly == null) {
            return data.size();
        } else if (headerFly != null && footerFly != null) {
            return data.size() + HEADER_VIEW_COUNT + FOOTER_VIEW_COUNT;
        } else {
            return data.size() + HEADER_VIEW_COUNT;
        }
    }

    public T getItem(int position) {
        if (position >= data.size()) return null;
        return data.get(position);
    }

    @Override
    public H onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view;
        if (isHeaderViewType(viewType)) {
            view = headerFly;
        } else if (isFooterViewType(viewType)) {
            view = footerFly;
        } else {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(layoutResId, viewGroup, false);
        }
        view.setOnClickListener(this);
        return makeViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(H helper, int position) {
        helper.itemView.setTag(position);
        if (isHeaderViewTypeByPos(position)) {
            headerConvert(helper);
        } else if (isFooterViewTypeByPos(position)) {
            footerConvert(helper);
        } else {
            T item = getItem(headerFly != null ? position - HEADER_VIEW_COUNT : position);
            contentConvert(helper, item);
        }
    }

    protected boolean isHeaderViewTypeByPos(int position) {
        return headerFly != null && getItemViewType(position) == MODE_HEADER;
    }

    protected boolean isFooterViewTypeByPos(int position) {
        return footerFly != null && getItemViewType(position) == MODE_FOOTER;
    }

    protected boolean isHeaderViewType(int viewType) {
        return headerFly != null && viewType == MODE_HEADER;
    }

    protected boolean isFooterViewType(int viewType) {
        return footerFly != null && viewType == MODE_FOOTER;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(v, (int) v.getTag());
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    /**
     * Implement this method and use the helper to adapt the view to the given item.
     *
     * @param helper A fully initialized helper.
     * @param item   The item that needs to be displayed.
     */
    protected abstract void contentConvert(H helper, T item);

    protected abstract H makeViewHolder(View view, int viewType);

    protected void headerConvert(H helper) {

    }

    protected void footerConvert(H helper) {

    }

}
