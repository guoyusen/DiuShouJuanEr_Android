package com.bili.diushoujuaner.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.bili.diushoujuaner.R;
import com.bili.diushoujuaner.adapter.viewholder.ViewHolder;
import com.bili.diushoujuaner.callback.OnContactAddListener;
import com.bili.diushoujuaner.model.tempHelper.ContactTemper;
import com.bili.diushoujuaner.utils.CommonUtil;
import com.bili.diushoujuaner.utils.ConstantUtil;
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
            if(type == ConstantUtil.CONTACT_ADD_FRIEND){
                ((TextView)holder.getView(R.id.txtBar)).setText("好友申请");
                holder.getView(R.id.txtTip).setVisibility(View.GONE);
                holder.getView(R.id.btnAgree).setEnabled(true);
                ((TextView)holder.getView(R.id.btnAgree)).setText("同意");
                ((TextView)holder.getView(R.id.btnAgree)).setTextColor(ContextCompat.getColor(context, R.color.TC_12B7F5));
            }else if (type == ConstantUtil.CONTACT_ADD_PARTY){
                ((TextView)holder.getView(R.id.txtBar)).setText("加群申请");
                holder.getView(R.id.txtTip).setVisibility(View.VISIBLE);
                ((TextView)holder.getView(R.id.txtTip)).setText("申请加入 " + ContactTemper.getInstance().getPartyName(applyVo.getToNo()));
                (holder.getView(R.id.btnAgree)).setEnabled(true);
                ((TextView)holder.getView(R.id.btnAgree)).setText("同意");
                ((TextView)holder.getView(R.id.btnAgree)).setTextColor(ContextCompat.getColor(context, R.color.TC_12B7F5));
            }else if (type == ConstantUtil.CONTACT_ADDED){
                ((TextView)holder.getView(R.id.txtBar)).setText("已添加");
                if(applyVo.getType() == ConstantUtil.CHAT_PARTY_APPLY){
                    holder.getView(R.id.txtTip).setVisibility(View.VISIBLE);
                    ((TextView)holder.getView(R.id.txtTip)).setText("申请加入 " + ContactTemper.getInstance().getPartyName(applyVo.getToNo()));
                }else{
                    holder.getView(R.id.txtTip).setVisibility(View.GONE);
                }
                (holder.getView(R.id.btnAgree)).setEnabled(false);
                ((TextView)holder.getView(R.id.btnAgree)).setText("已添加");
                ((TextView)holder.getView(R.id.btnAgree)).setTextColor(ContextCompat.getColor(context, R.color.TC_ADADBB));
            }else if(type == ConstantUtil.CONTACT_MAY_KNOW){
                ((TextView)holder.getView(R.id.txtBar)).setText("童友推荐");
                holder.getView(R.id.txtTip).setVisibility(View.GONE);
                (holder.getView(R.id.btnAgree)).setEnabled(true);
                ((TextView)holder.getView(R.id.btnAgree)).setText("添加");
                ((TextView)holder.getView(R.id.btnAgree)).setTextColor(ContextCompat.getColor(context, R.color.TC_12B7F5));
            }
            CommonUtil.displayDraweeView(applyVo.getPicPath(), (SimpleDraweeView)holder.getView(R.id.ivHead));
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
            return ConstantUtil.CONTACT_ADDED;
        }else if (!list.get(position).getAccept() && list.get(position).getType() == ConstantUtil.CHAT_FRIEND_APPLY){
            return ConstantUtil.CONTACT_ADD_FRIEND;
        }else if(!list.get(position).getAccept() && list.get(position).getType() == ConstantUtil.CHAT_PARTY_APPLY){
            return ConstantUtil.CONTACT_ADD_PARTY;
        }else if (list.get(position).getType() == ConstantUtil.CHAT_FRIEND_RECOMMEND){
            return ConstantUtil.CONTACT_MAY_KNOW;
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
