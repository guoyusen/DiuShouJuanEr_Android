package com.bili.diushoujuaner.adapter;

import android.content.Context;

import com.bili.diushoujuaner.R;
import com.bili.diushoujuaner.adapter.viewholder.ViewHolder;
import com.bili.diushoujuaner.utils.response.PartyDto;

import java.util.List;

/**
 * Created by BiLi on 2016/3/9.
 */
public class PartyAdapter extends CommonAdapter<PartyDto> {

    public PartyAdapter (Context context, List<PartyDto> list){
        super(context, list, R.layout.item_main_party);
    }

    @Override
    public void convert(ViewHolder holder, PartyDto partyDto, int position) throws Exception {
        if(partyDto != null){

        }
    }
}
