package com.bili.diushoujuaner.adapter;

import android.content.Context;

import com.bili.diushoujuaner.R;
import com.bili.diushoujuaner.adapter.viewholder.ViewHolder;
import com.bili.diushoujuaner.utils.response.MessageDto;

import java.util.List;

/**
 * Created by BiLi on 2016/3/5.
 */
public class MessageAdapter extends CommonAdapter<MessageDto> {

    public MessageAdapter(Context context, List<MessageDto> list){
        super(context,list, R.layout.item_main_message);
    }

    @Override
    public void convert(ViewHolder holder, MessageDto messageDto, int position) throws Exception {
        if(messageDto != null){

        }
    }
}
