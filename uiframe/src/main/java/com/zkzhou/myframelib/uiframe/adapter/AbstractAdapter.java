
package com.zkzhou.myframelib.uiframe.adapter;

import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 抽象适配器.提供修改适配器数据的方法，这些方法必须运行在UI线程中，否则会抛出异常
 *
 * @param <T>
 */
public abstract class AbstractAdapter<T> extends BaseAdapter {

    protected ArrayList<T> mDatas = new ArrayList<T>();

    public AbstractAdapter() {
    }

    public AbstractAdapter(List<T> datas) {
        setData(datas);
    }

    public AbstractAdapter(T[] datas) {
        setData(datas);
    }

    public void setData(List<T> datas) {
        this.mDatas.clear();
        if (datas != null) {
            this.mDatas.addAll(datas);
        }
    }

    public void setData(T[] datas) {
        this.mDatas.clear();
        if (datas != null) {
            for (T t : datas) {
                this.mDatas.add(t);
            }
        }
    }

    public void addData(List<T> datas) {
        if (datas != null && datas.size() > 0) {
            this.mDatas.addAll(datas);
        }
    }

    public void addData(T[] datas) {
        if (datas != null && datas.length > 0) {
            for (T data : datas) {
                this.mDatas.add(data);
            }
        }
    }

    public void addData(T data) {
        if (data != null) {
            this.mDatas.add(data);
        }
    }

    public void addData(int index, T data) {
        if (index >= 0 && index <= mDatas.size()) {
            if (data != null) {
                this.mDatas.add(index, data);
            }
        }
    }

    public void addData(int index, List<T> datas) {
        if (index >= 0 && index <= mDatas.size()) {
            if (datas != null && datas.size() > 0) {
                this.mDatas.addAll(index, datas);
            }
        }
    }

    public void removeData(T data) {
        if (data != null) {
            this.mDatas.remove(data);
        }
    }

    public void removeData(int index) {
        if (index >= 0 && index < mDatas.size()) {
            this.mDatas.remove(index);
        }
    }

    public void clearData() {
        this.mDatas.clear();
    }

    public ArrayList<T> getDatas() {
        return this.mDatas;
    }

    public abstract T[] getDatasOfArray();

    @Override
    public int getCount() {
        if (mDatas == null) {
            return 0;
        }
        return this.mDatas.size();
    }

    @Override
    public T getItem(int position) {
        if (position >= 0 && position < this.mDatas.size()) {
            return this.mDatas.get(position);
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        // 简单返回数据索引号，可以重写本方法，返回真实数据对应的ID
        return position;
    }

}
