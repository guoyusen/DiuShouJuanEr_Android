package com.bili.diushoujuaner.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.bili.diushoujuaner.R;
import com.bili.diushoujuaner.adapter.viewholder.ViewHolder;
import com.bili.diushoujuaner.utils.Common;
import com.bili.diushoujuaner.utils.Constant;
import com.bili.diushoujuaner.utils.entity.dto.ContactDto;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.HashMap;
import java.util.List;

/**
 * Created by BiLi on 2016/4/23.
 */
public class ContactSearchAdapter extends CommonAdapter<ContactDto> {

    private HashMap<Integer, Integer> barMap;

    public ContactSearchAdapter(Context context, List<ContactDto> list) {
        super(context, list, R.layout.item_contact_search);
        barMap = new HashMap<>();
    }

    @Override
    public void convert(ViewHolder holder, ContactDto contactDto, int position) throws Exception {
        if(contactDto != null){
            int type = getContTypeForPosition(position);
            if(position == getPositionForConType(type)){
                holder.getView(R.id.txtBar).setVisibility(View.VISIBLE);
                if(type == Constant.CONTACT_FRIEND){
                    ((TextView)holder.getView(R.id.txtBar)).setText("搜索到的小伙伴");
                }else{
                    ((TextView)holder.getView(R.id.txtBar)).setText("搜索到的群组");
                }
            }else{
                holder.getView(R.id.txtBar).setVisibility(View.GONE);
            }
            Common.displayDraweeView(contactDto.getPicPath(), (SimpleDraweeView)holder.getView(R.id.ivHead));
            if(type == Constant.CONTACT_FRIEND){
                ((TextView)holder.getView(R.id.txtName)).setText(contactDto.getNickName());
                ((TextView)holder.getView(R.id.txtBrief)).setText(contactDto.getAutograph());
            }else{
                ((TextView)holder.getView(R.id.txtName)).setText(contactDto.getDisplayName());
                ((TextView)holder.getView(R.id.txtBrief)).setText(contactDto.getInformation());
            }

        }
    }

    private int getPositionForConType(int type){
        if(barMap.get(type) != null){
            return barMap.get(type);
        }
        for(int i = 0, len = list.size(); i < len; i++){
            if(list.get(i).getType() == type){
                barMap.put(type, i);
                return i;
            }
        }
        return -1;
    }

    private int getContTypeForPosition(int position){
        return list.get(position).getType();
    }
}
