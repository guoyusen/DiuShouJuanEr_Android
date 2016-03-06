package com.bili.diushoujuaner.adapter;

import android.content.Context;

import com.bili.diushoujuaner.R;
import com.bili.diushoujuaner.adapter.viewholder.ViewHolder;
import com.bili.diushoujuaner.model.response.RecallVo;

import java.util.List;

/**
 * Created by BiLi on 2016/3/5.
 */
public class RecallAdapter extends CommonAdapter<RecallVo> {

    public RecallAdapter(Context context, List<RecallVo> list){
        super(context,list, R.layout.layout_item_recall);
    }

    @Override
    public void convert(ViewHolder holder, RecallVo recallVo, int position) throws Exception {
        if(recallVo != null){

        }
    }
}
