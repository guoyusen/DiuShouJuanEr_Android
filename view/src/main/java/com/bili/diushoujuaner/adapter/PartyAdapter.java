package com.bili.diushoujuaner.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.bili.diushoujuaner.R;
import com.bili.diushoujuaner.adapter.viewholder.ViewHolder;
import com.bili.diushoujuaner.model.tempHelper.ContactTemper;
import com.bili.diushoujuaner.utils.Common;
import com.bili.diushoujuaner.utils.entity.vo.PartyVo;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by BiLi on 2016/3/6.
 */
public class PartyAdapter extends CommonAdapter<PartyVo> {

    private Map<Integer,Integer> sectionMap;

    public PartyAdapter(Context context, List<PartyVo> list){
        super(context, list, R.layout.item_main_contact);
        sectionMap = new HashMap<>();
    }

    @Override
    public void convert(ViewHolder holder, PartyVo partyVo, int position) throws Exception {
        if(partyVo != null){
            int section = getSectionForPosition(position);
            if(position == getPositionForSection(section)){
                holder.getView(R.id.txtItemMark).setVisibility(View.VISIBLE);
                holder.getView(R.id.line).setVisibility(View.VISIBLE);
                ((TextView) holder.getView(R.id.txtItemMark)).setText(partyVo.getSortLetter());
            }else{
                holder.getView(R.id.txtItemMark).setVisibility(View.INVISIBLE);
                holder.getView(R.id.line).setVisibility(View.INVISIBLE);
            }
            Common.displayDraweeView(partyVo.getPicPath(), (SimpleDraweeView) holder.getView(R.id.ivItemHead));
            String displayName = ContactTemper.getInstance().getPartyName(partyVo.getPartyNo());
            ((TextView) holder.getView(R.id.txtContactName)).setText(displayName == null ? partyVo.getDisplayName() : displayName);
        }
    }

    public int getSectionForPosition(int position) {
        return list.get(position).getSortLetter().charAt(0);
    }

    public int getPositionForSection(int section) {
        if(sectionMap.get(section) != null){
            return sectionMap.get(section).intValue();
        }
        for (int i = 0; i < getCount(); i++) {
            String sortStr = list.get(i).getSortLetter();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                sectionMap.put(section, i);
                return i;
            }
        }
        return -1;
    }

    @Override
    public void refresh(List<PartyVo> list) {
        sectionMap.clear();
        super.refresh(list);
    }
}
