package com.bili.diushoujuaner.adapter.viewholder;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by BiLi on 2016/3/5.
 */
public class ViewHolder {

    private SparseArray<View> views;
    private int position;
    private View convertView;

    public View getmConvertView() {
        return convertView;
    }

    public ViewHolder(Context context, ViewGroup parent, int layoutId,
                      int position) {
        this.position = position;
        this.views = new SparseArray<View>();
        convertView = LayoutInflater.from(context).inflate(layoutId, parent,
                false);
        convertView.setTag(this);
    }

    public static ViewHolder get(Context context, View convertView,
                                 ViewGroup parent, int layoutId, int position) {
        if (convertView == null) {

            return new ViewHolder(context, parent, layoutId, position);

        } else {
            ViewHolder holder = (ViewHolder) convertView.getTag();
            holder.position = position;
            return holder;
        }

    }

    public <T extends View> T getView(int viewId) {
        View view = views.get(viewId);
        if (view == null) {
            view = convertView.findViewById(viewId);
            views.put(viewId, view);
        }
        return (T) view;
    }
}
