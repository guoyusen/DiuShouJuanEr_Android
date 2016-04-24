package com.bili.diushoujuaner.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.bili.diushoujuaner.R;
import com.bili.diushoujuaner.adapter.viewholder.ViewHolder;
import com.bili.diushoujuaner.callback.OnContactAddListener;
import com.bili.diushoujuaner.model.tempHelper.ContactTemper;
import com.bili.diushoujuaner.utils.Common;
import com.bili.diushoujuaner.utils.Constant;
import com.bili.diushoujuaner.utils.entity.vo.ApplyVo;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.HashMap;
import java.util.List;

/**
 * Created by BiLi on 2016/4/24.
 */
public class ApplyAdapter extends CommonAdapter<ApplyVo> {

    private HashMap<Integer, Integer> hashMap;
    private OnContactAddListener contactAddListener;

    public ApplyAdapter(Context context, List<ApplyVo> list) {
        super(context, list, R.layout.item_contact_add);
        hashMap = new HashMap<>();
    }

    public void setContactAddListener(OnContactAddListener contactAddListener) {
        this.contactAddListener = contactAddListener;
    }

    @Override
    public void refresh(List<ApplyVo> list) {
        super.refresh(list);
        hashMap.clear();
    }

    @Override
    public void convert(ViewHolder holder, ApplyVo applyVo, final int position) throws Exception {
        if(applyVo != null){
            int type = getTypeForPosition(position);
            if(position == getPositionForType(type)){
                holder.getView(R.id.txtBar).setVisibility(View.VISIBLE);
            }else{
                holder.getView(R.id.txtBar).setVisibility(View.GONE);
            }
            if(type == Constant.CONTACT_ADD_FRIEND){
                ((TextView)holder.getView(R.id.txtBar)).setText("好友申请");
                holder.getView(R.id.txtTip).setVisibility(View.GONE);
                holder.getView(R.id.btnAgree).setEnabled(true);
                ((TextView)holder.getView(R.id.btnAgree)).setText("同意");
                ((TextView)holder.getView(R.id.btnAgree)).setTextColor(ContextCompat.getColor(context, R.color.TC_12B7F5));
            }else if (type == Constant.CONTACT_ADD_PARTY){
                ((TextView)holder.getView(R.id.txtBar)).setText("加群申请");
                holder.getView(R.id.txtTip).setVisibility(View.VISIBLE);
                ((TextView)holder.getView(R.id.txtTip)).setText("申请加入 " + ContactTemper.getInstance().getPartyName(applyVo.getToNo()));
                (holder.getView(R.id.btnAgree)).setEnabled(true);
                ((TextView)holder.getView(R.id.btnAgree)).setText("同意");
                ((TextView)holder.getView(R.id.btnAgree)).setTextColor(ContextCompat.getColor(context, R.color.TC_12B7F5));
            }else if (type == Constant.CONTACT_ADDED){
                ((TextView)holder.getView(R.id.txtBar)).setText("已同意加入");
                holder.getView(R.id.txtTip).setVisibility(View.GONE);
                (holder.getView(R.id.btnAgree)).setEnabled(false);
                ((TextView)holder.getView(R.id.btnAgree)).setText("已添加");
                ((TextView)holder.getView(R.id.btnAgree)).setTextColor(ContextCompat.getColor(context, R.color.TC_ADADBB));
            }else if(type == Constant.CONTACT_MAY_KNOW){
                ((TextView)holder.getView(R.id.txtBar)).setText("您可能认识");
                holder.getView(R.id.txtTip).setVisibility(View.GONE);
                (holder.getView(R.id.btnAgree)).setEnabled(true);
                ((TextView)holder.getView(R.id.btnAgree)).setText("添加");
                ((TextView)holder.getView(R.id.btnAgree)).setTextColor(ContextCompat.getColor(context, R.color.TC_12B7F5));
            }
            Common.displayDraweeView(applyVo.getPicPath(), (SimpleDraweeView)holder.getView(R.id.ivHead));
            ((TextView)holder.getView(R.id.txtName)).setText(applyVo.getUserName());
            ((TextView)holder.getView(R.id.txtApply)).setText("我是 " + applyVo.getContent());
            holder.getView(R.id.btnAgree).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(contactAddListener != null){
                        contactAddListener.onContactAdd(position);
                    }
                }
            });
        }
    }

    private int getTypeForPosition(int position){
        if(list.get(position).getAccept()){
            return Constant.CONTACT_ADDED;
        }else if (!list.get(position).getAccept() && list.get(position).getType() == Constant.CHAT_FRIEND_ADD){
            return Constant.CONTACT_ADD_FRIEND;
        }else if(!list.get(position).getAccept() && list.get(position).getType() == Constant.CHAT_PARTY_ADD){
            return Constant.CONTACT_ADD_PARTY;
        }else if (list.get(position).getType() == Constant.CHAT_FRIEND_RECOMMEND){
            return Constant.CONTACT_MAY_KNOW;
        }else{
            return -1;
        }
    }

    private int getPositionForType(int type){
        if(hashMap.get(type) != null){
            return hashMap.get(type);
        }
        for(int i = 0, len = list.size(); i < len; i++){
            if(getTypeForPosition(i) == type){
                hashMap.put(type, i);
                return i;
            }
        }
        return -1;
    }
}