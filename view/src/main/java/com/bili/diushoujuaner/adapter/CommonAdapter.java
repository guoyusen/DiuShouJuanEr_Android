package com.bili.diushoujuaner.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.bili.diushoujuaner.adapter.viewholder.ViewHolder;

import java.util.List;

/**
 * Created by BiLi on 2016/3/5.
 */
public abstract class CommonAdapter<T> extends BaseAdapter {

    protected Context context;
    protected List<T> list;
    protected LayoutInflater mInflater;
    protected int layoutId;

    public CommonAdapter(Context context, List<T> list, int layoutId) {
        this.context = context;
        this.list = list;
        this.layoutId = layoutId;
        mInflater = LayoutInflater.from(context);
    }

    public CommonAdapter(Context context, int layoutId) {
        this.context = context;
        this.layoutId = layoutId;
        mInflater = LayoutInflater.from(context);
    }

    public boolean isEmpty(){
        return this.list.isEmpty();
    }

    /**
     * 设置为新的数据，旧数据会被清空
     *
     * @param list
     */
    public void refresh(List<T> list) {
        if (list == null || list.size() <= 0) {
            return;
        }
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    /**
     * 在原有的数据上添加新数据
     *
     * @param list
     */
    public void add(List<T> list) {
        if (list == null || list.size() <= 0) {
            return;
        }
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    /**
     * 清空数据
     */
    public void clear() {
        if (list == null || list.size() <= 0) {
            return;
        }
        this.list.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return this.list.size();
    }

    @Override
    public T getItem(int position) {
        return this.list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = ViewHolder.get(context, convertView, parent, layoutId, position);
        try {
            convert(holder, getItem(position), position);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return holder.getmConvertView();
    }

    public abstract void convert(ViewHolder holder, T t, int position) throws Exception;
}
