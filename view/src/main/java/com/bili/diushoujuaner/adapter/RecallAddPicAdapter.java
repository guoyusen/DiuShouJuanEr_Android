package com.bili.diushoujuaner.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.bili.diushoujuaner.R;
import com.bili.diushoujuaner.adapter.viewholder.ViewHolder;
import com.bili.diushoujuaner.utils.Constant;
import com.bili.diushoujuaner.utils.entity.RecallAddPicVo;

import java.io.FileInputStream;
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
            if(pictureVo.getType() == Constant.RECALL_ADD_PIC_PATH){
                ((ImageView)holder.getView(R.id.itemImg)).setImageBitmap(BitmapFactory.decodeStream(new FileInputStream(pictureVo.getPath())));
            }else if(pictureVo.getType() == Constant.RECALL_ADD_PIC_RES){
                ((ImageView)holder.getView(R.id.itemImg)).setImageResource(pictureVo.getResourceId());
            }
        }
    }
}
