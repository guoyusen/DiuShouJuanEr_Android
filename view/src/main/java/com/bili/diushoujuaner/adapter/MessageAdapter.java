package com.bili.diushoujuaner.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bili.diushoujuaner.R;
import com.bili.diushoujuaner.activity.ContactDetailActivity;
import com.bili.diushoujuaner.activity.SpaceActivity;
import com.bili.diushoujuaner.adapter.viewholder.ViewHolder;
import com.bili.diushoujuaner.callback.OnReSendListener;
import com.bili.diushoujuaner.model.actionhelper.action.UserInfoAction;
import com.bili.diushoujuaner.model.preferhelper.CustomSessionPreference;
import com.bili.diushoujuaner.model.tempHelper.ContactTemper;
import com.bili.diushoujuaner.utils.Common;
import com.bili.diushoujuaner.utils.Constant;
import com.bili.diushoujuaner.utils.entity.vo.FriendVo;
import com.bili.diushoujuaner.utils.entity.vo.MemberVo;
import com.bili.diushoujuaner.utils.entity.vo.MessageVo;
import com.bili.diushoujuaner.widget.RotateImageView;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by BiLi on 2016/4/15.
 */
public class MessageAdapter extends BaseAdapter {

    private static final int VIEW_TYPE_CHAT_LEFT = 0;
    private static final int VIEW_TYPE_CHAT_RIGHT = 1;

    private Context context;
    private List<MessageVo> messageVoList;
    private long ownerNo;
    private OnReSendListener reSendListener;

    public MessageAdapter(Context context, List<MessageVo> messageVoList, long ownerNo) {
        this.context = context;
        this.messageVoList = messageVoList;
        this.ownerNo = ownerNo;
    }

    public void setReSendListener(OnReSendListener reSendListener) {
        this.reSendListener = reSendListener;
    }

    public void addFirst(List<MessageVo> messageVoList){
        if(messageVoList == null || messageVoList.isEmpty()){
            return;
        }
        this.messageVoList.addAll(0, messageVoList);
        notifyDataSetInvalidated();
    }

    public void addLast(MessageVo messageVo){
        if(messageVo == null){
            return;
        }
        this.messageVoList.add(messageVo);
        notifyDataSetChanged();
    }

    public void refresh(List<MessageVo> messageVoList){
        if(messageVoList == null){
            return;
        }
        this.messageVoList.clear();
        this.messageVoList.addAll(messageVoList);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if(messageVoList.get(position).getFromNo() == this.ownerNo){
            return VIEW_TYPE_CHAT_RIGHT;
        }else{
            return VIEW_TYPE_CHAT_LEFT;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getCount() {
        return messageVoList.size();
    }

    @Override
    public Object getItem(int position) {
        return messageVoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(messageVoList.get(position).getFromNo() == this.ownerNo){
            return getItemRightView(position, convertView, parent);
        }else{
            return getItemLeftView(position, convertView, parent);
        }
    }

    private View getItemRightView(int position, View convertView, ViewGroup parent){
        ViewHolder holder = ViewHolder.get(context, convertView, parent, R.layout.item_message_right, position);
        setViewStatus(holder, messageVoList.get(position));
        return holder.getmConvertView();
    }

    private View getItemLeftView(int position, View convertView, ViewGroup parent){
        ViewHolder holder = ViewHolder.get(context, convertView, parent, R.layout.item_message_left, position);
        setViewStatus(holder, messageVoList.get(position));
        return holder.getmConvertView();
    }

    private void setViewStatus(ViewHolder holder, final MessageVo messageVo){
        if(messageVo != null){
            setTime(holder, messageVo);
            setNickName(holder, messageVo);
            setClickListener(holder, messageVo);
            setMessageContent(holder, messageVo);
            setMessageVoStatus(holder, messageVo);
        }
    }

    private void setTime(ViewHolder holder, final MessageVo messageVo){
        if(messageVo.isTimeShow()){
            ((TextView)holder.getView(R.id.txtTime)).setText(Common.getFormatTime(messageVo.getTime()));
            holder.getView(R.id.txtTime).setVisibility(View.VISIBLE);
        }else{
            holder.getView(R.id.txtTime).setVisibility(View.GONE);
        }
    }

    private void setNickName(ViewHolder holder, final MessageVo messageVo){
        if(messageVo.getMsgType() == Constant.CHAT_FRI){
            holder.getView(R.id.txtName).setVisibility(View.GONE);
            if(messageVo.getFromNo() == CustomSessionPreference.getInstance().getCustomSession().getUserNo()){
                String picPath = UserInfoAction.getInstance(context).getUserFromLocal().getPicPath();
                Common.displayDraweeView(picPath != null ? picPath : "", (SimpleDraweeView)holder.getView(R.id.ivHead));
            }else{
                FriendVo friendVo = ContactTemper.getInstance().getFriendVo(messageVo.getFromNo());
                Common.displayDraweeView(friendVo != null ? friendVo.getPicPath() : "", (SimpleDraweeView)holder.getView(R.id.ivHead));
            }
        }else if(messageVo.getMsgType() == Constant.CHAT_PAR){
            holder.getView(R.id.txtName).setVisibility(View.VISIBLE);
            MemberVo memberVo = ContactTemper.getInstance().getMemberVo(messageVo.getToNo(), messageVo.getFromNo());
            ((TextView)holder.getView(R.id.txtName)).setText(memberVo != null ? memberVo.getMemberName() : "");
            Common.displayDraweeView(memberVo != null ? memberVo.getPicPath() : "", (SimpleDraweeView)holder.getView(R.id.ivHead));
        }
    }

    private void setClickListener(ViewHolder holder, final MessageVo messageVo){
        holder.getView(R.id.ivHead).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(messageVo.getFromNo() == CustomSessionPreference.getInstance().getCustomSession().getUserNo())){
                    context.startActivity(new Intent(context, ContactDetailActivity.class).putExtra(ContactDetailActivity.TAG, messageVo.getFromNo()));
                }
            }
        });
        holder.getView(R.id.ivFail).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(reSendListener != null){
                    reSendListener.onReSendMessageVo(messageVo);
                }
            }
        });
    }

    private void setMessageContent(ViewHolder holder, MessageVo messageVo){
        //设置消息内容
        switch (messageVo.getConType()){
            case Constant.CHAT_CONTENT_TEXT:
                ((TextView)holder.getView(R.id.txtContent)).setText(messageVo.getContent());
                break;
            case Constant.CHAT_CONTENT_IMG:

                break;
            case Constant.CHAT_CONTENT_VOICE:

                break;
        }
    }

    private void setMessageVoStatus(ViewHolder holder, MessageVo messageVo){
        switch (messageVo.getStatus()){
            case Constant.MESSAGE_STATUS_FAIL:
                holder.getView(R.id.ivFail).setVisibility(View.VISIBLE);
                holder.getView(R.id.ivSending).setVisibility(View.GONE);
                ((RotateImageView)holder.getView(R.id.ivSending)).stopRotate(true);
                break;
            case Constant.MESSAGE_STATUS_SENDING:
                holder.getView(R.id.ivFail).setVisibility(View.GONE);
                holder.getView(R.id.ivSending).setVisibility(View.VISIBLE);
                ((RotateImageView)holder.getView(R.id.ivSending)).stopRotate(false);
                break;
            case Constant.MESSAGE_STATUS_SUCCESS:
                holder.getView(R.id.ivFail).setVisibility(View.GONE);
                holder.getView(R.id.ivSending).setVisibility(View.GONE);
                ((RotateImageView)holder.getView(R.id.ivSending)).stopRotate(true);
                break;
        }
    }
}
