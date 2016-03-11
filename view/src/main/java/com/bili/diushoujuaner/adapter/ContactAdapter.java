package com.bili.diushoujuaner.adapter;

import android.content.Context;

import com.bili.diushoujuaner.R;
import com.bili.diushoujuaner.adapter.viewholder.ViewHolder;
import com.bili.diushoujuaner.utils.response.ContactDto;

import java.util.List;

/**
 * Created by BiLi on 2016/3/6.
 */
public class ContactAdapter extends CommonAdapter<ContactDto> {

    public ContactAdapter(Context context, List<ContactDto> list){
        super(context, list, R.layout.item_main_contact);
    }

    @Override
    public void convert(ViewHolder holder, ContactDto contactDto, int position) throws Exception {
        if(contactDto != null){

        }
    }
}
