package com.bili.diushoujuaner.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.bili.diushoujuaner.R;
import com.bili.diushoujuaner.adapter.viewholder.ViewHolder;
import com.bili.diushoujuaner.utils.CommonUtil;
import com.bili.diushoujuaner.utils.entity.vo.FriendVo;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by BiLi on 2016/3/6.
 */
public class ContactAdapter extends CommonAdapter<FriendVo> {

    private Map<Integer,Integer> sectionMap;

    public ContactAdapter(Context context, List<FriendVo> list){
        super(context, list, R.layout.item_main_contact);
        sectionMap = new HashMap<>();
    }

    @Override
    public void convert(ViewHolder holder, FriendVo friendVo, int position) throws Exception {
        if(friendVo != null){
            int section = getSectionForPosition(position);
            if(position == getPositionForSection(section)){
                holder.getView(R.id.txtItemMark).setVisibility(View.VISIBLE);
                holder.getView(R.id.line).setVisibility(View.VISIBLE);
                ((TextView) holder.getView(R.id.txtItemMark)).setText(friendVo.getSortLetter());
            }else{
                holder.getView(R.id.txtItemMark).setVisibility(View.INVISIBLE);
                holder.getView(R.id.line).setVisibility(View.INVISIBLE);
            }
            CommonUtil.displayDraweeView(friendVo.getPicPath(), (SimpleDraweeView) holder.getView(R.id.ivItemHead));
            ((TextView) holder.getView(R.id.txtContactName)).setText(friendVo.getDisplayName());
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
    public void refresh(List<FriendVo> list) {
        sectionMap.clear();
        super.refresh(list);
    }
}
