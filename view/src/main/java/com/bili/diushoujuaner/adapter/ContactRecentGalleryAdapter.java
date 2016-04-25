package com.bili.diushoujuaner.adapter;

import android.content.Context;

import com.bili.diushoujuaner.R;
import com.bili.diushoujuaner.adapter.viewholder.ViewHolder;
import com.bili.diushoujuaner.utils.entity.dto.PictureDto;
import com.bili.diushoujuaner.utils.CommonUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by BiLi on 2016/3/8.
 */
public class ContactRecentGalleryAdapter extends CommonAdapter<PictureDto> {

    public ContactRecentGalleryAdapter(Context context, List<PictureDto> list){
        super(context, list, R.layout.item_recent_publish);
    }

    @Override
    public void convert(ViewHolder holder, PictureDto picture, int position) throws Exception {
        if(picture != null){
            CommonUtil.displayDraweeView(picture.getPicPath(),((SimpleDraweeView)holder.getView(R.id.ivItem)));
        }
    }
}
