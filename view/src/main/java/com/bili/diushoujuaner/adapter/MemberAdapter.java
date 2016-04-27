package com.bili.diushoujuaner.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.bili.diushoujuaner.R;
import com.bili.diushoujuaner.adapter.viewholder.ViewHolder;
import com.bili.diushoujuaner.callback.OnDeleteMemberListener;
import com.bili.diushoujuaner.utils.CommonUtil;
import com.bili.diushoujuaner.utils.ConstantUtil;
import com.bili.diushoujuaner.utils.entity.vo.MemberVo;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.HashMap;
import java.util.List;

/**
 * Created by BiLi on 2016/4/26.
 */
public class MemberAdapter extends CommonAdapter<MemberVo> {

    private boolean isEditable = false;
    private HashMap<Integer, Integer> hashMap;
    private OnDeleteMemberListener deleteMemberListener;

    public MemberAdapter(Context context, List<MemberVo> list) {
        super(context, list, R.layout.item_member);
        hashMap = new HashMap<>();
    }

    public void setDeleteMemberListener(OnDeleteMemberListener deleteMemberListener) {
        this.deleteMemberListener = deleteMemberListener;
    }

    public boolean isEditable() {
        return isEditable;
    }

    public void setEditable(boolean editable) {
        isEditable = editable;
    }

    @Override
    public void convert(ViewHolder holder, final MemberVo memberVo, int position) throws Exception {
        if(memberVo != null){
            int section = getSectionForPosition(position);
            if(position == getPositionForSection(section)){
                holder.getView(R.id.txtItemMark).setVisibility(View.VISIBLE);
                ((TextView)holder.getView(R.id.txtItemMark)).setText(memberVo.getSortLetter());
            }else{
                holder.getView(R.id.txtItemMark).setVisibility(View.GONE);
            }
            if(memberVo.getType() == ConstantUtil.CONTACT_FRIEND){
                holder.getView(R.id.txtOwner).setVisibility(View.GONE);
            }else if(memberVo.getType() == ConstantUtil.CONTACT_PARTY){
                holder.getView(R.id.txtOwner).setVisibility(View.VISIBLE);
            }
            if(isEditable && memberVo.getType() == ConstantUtil.CONTACT_FRIEND){
                holder.getView(R.id.layoutDelete).setVisibility(View.VISIBLE);
            }else{
                holder.getView(R.id.layoutDelete).setVisibility(View.GONE);
            }
            ((TextView)holder.getView(R.id.txtMemberName)).setText(memberVo.getMemberName());
            CommonUtil.displayDraweeView(memberVo.getPicPath(), (SimpleDraweeView)holder.getView(R.id.ivItemHead));
            holder.getView(R.id.layoutDelete).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(deleteMemberListener!= null){
                        deleteMemberListener.onDeleteMember(memberVo.getUserNo());
                    }
                }
            });
        }
    }

    public int getSectionForPosition(int position) {
        return list.get(position).getSortLetter().charAt(0);
    }

    public int getPositionForSection(int section) {
        if(hashMap.get(section) != null){
            return hashMap.get(section);
        }
        for (int i = 0; i < getCount(); i++) {
            char firstChar = list.get(i).getSortLetter().toUpperCase().charAt(0);
            if (firstChar == section) {
                hashMap.put(section, i);
                return i;
            }
        }
        return -1;
    }

}
