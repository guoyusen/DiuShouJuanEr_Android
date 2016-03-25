package com.bili.diushoujuaner.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bili.diushoujuaner.R;
import com.bili.diushoujuaner.utils.Imageloader;
import com.bili.diushoujuaner.utils.entity.PictureVo;
import com.bili.diushoujuaner.widget.picture.PhotoFrescoView;

import java.util.List;

/**
 * Created by BiLi on 2016/3/25.
 */
public class ImageAdapter extends BaseAdapter {

    private Context context;
    private List<PictureVo> pictureVoList;
    private int margin;

    public ImageAdapter(Context context, List<PictureVo> pictureVoList){
        this.context = context;
        this.pictureVoList = pictureVoList;
        this.margin = (int)context.getResources().getDimension(R.dimen.x8);
    }

    @Override
    public int getCount() {
        return this.pictureVoList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final PhotoFrescoView photoView = new PhotoFrescoView(this.context);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int)context.getResources().getDimension(R.dimen.y168));

        photoView.setLayoutParams(lp);
        photoView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        photoView.setEnabled(true);
        photoView.setPadding(this.margin,this.margin,this.margin,this.margin);
        photoView.setAspectRatio(1.4f);

        Imageloader.getInstance().displayDraweeView(pictureVoList.get(position).getPicPath(), photoView);

        photoView.touchEnable(false);
        return photoView;
    }
}
