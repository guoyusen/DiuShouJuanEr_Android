package com.bili.diushoujuaner.adapter;

import android.content.Context;
import android.net.Uri;

import com.bili.diushoujuaner.R;
import com.bili.diushoujuaner.adapter.viewholder.ViewHolder;
import com.bili.diushoujuaner.utils.CommonUtil;
import com.bili.diushoujuaner.utils.ConstantUtil;
import com.bili.diushoujuaner.utils.entity.vo.RecallAddPicVo;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by BiLi on 2016/3/25.
 */
public class RecallAddPicAdapter extends CommonAdapter<RecallAddPicVo> {

    public RecallAddPicAdapter(Context context, List<RecallAddPicVo> pictureVoList){
        super(context, pictureVoList, R.layout.item_recall_add_pic);
    }

    @Override
    public void convert(ViewHolder holder, RecallAddPicVo pictureVo, int position) throws Exception {
        if(pictureVo != null){
            if(pictureVo.getType() == ConstantUtil.RECALL_ADD_PIC_PATH){
                CommonUtil.displayDraweeViewFromLocal(pictureVo.getPath(), (SimpleDraweeView)holder.getView(R.id.itemImg));
            }else if(pictureVo.getType() == ConstantUtil.RECALL_ADD_PIC_RES){
                ((SimpleDraweeView)holder.getView(R.id.itemImg)).setImageURI(Uri.parse("res://com.bili.diushoujuaner/" + pictureVo.getResourceId()));
            }
        }
    }
}
