package com.bili.diushoujuaner.adapter;

import android.content.Context;

import com.bili.diushoujuaner.R;
import com.bili.diushoujuaner.adapter.viewholder.ViewHolder;
import com.bili.diushoujuaner.model.response.Picture;

import java.util.List;

/**
 * Created by BiLi on 2016/3/8.
 */
public class RecallDetailGalleryAdapter extends CommonAdapter<Picture> {

    public RecallDetailGalleryAdapter(Context context, List<Picture> list){
        super(context, list, R.layout.layout_item_gallery);
    }

    @Override
    public void convert(ViewHolder holder, Picture picture, int position) throws Exception {
        if(picture != null){

        }
    }
}
