package com.bili.diushoujuaner.adapter;

import android.content.Context;

import com.bili.diushoujuaner.R;
import com.bili.diushoujuaner.adapter.viewholder.ViewHolder;
import com.bili.diushoujuaner.utils.entity.dto.ContactDto;

import java.util.List;

/**
 * Created by BiLi on 2016/4/23.
 */
public class ContactSearchAdapter extends CommonAdapter<ContactDto> {

    public ContactSearchAdapter(Context context, List<ContactDto> list) {
        super(context, list, R.layout.item_contact_search);
    }

    @Override
    public void convert(ViewHolder holder, ContactDto contactDto, int position) throws Exception {
        if(contactDto != null){

        }
    }
}
