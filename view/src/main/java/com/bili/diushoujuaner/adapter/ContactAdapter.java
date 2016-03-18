package com.bili.diushoujuaner.adapter;

import android.content.Context;

import com.bili.diushoujuaner.R;
import com.bili.diushoujuaner.adapter.viewholder.ViewHolder;
import com.bili.diushoujuaner.utils.entity.FriendVo;

import java.util.List;

/**
 * Created by BiLi on 2016/3/6.
 */
public class ContactAdapter extends CommonAdapter<FriendVo> {

    public ContactAdapter(Context context, List<FriendVo> list){
        super(context, list, R.layout.item_main_contact);
    }

    @Override
    public void convert(ViewHolder holder, FriendVo friendVo, int position) throws Exception {
        if(friendVo != null){

        }
    }


}
