package com.bili.diushoujuaner.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bili.diushoujuaner.R;
import com.bili.diushoujuaner.adapter.viewholder.ViewHolder;
import com.bili.diushoujuaner.model.tempHelper.ContactTemper;
import com.bili.diushoujuaner.utils.Common;
import com.bili.diushoujuaner.utils.Constant;
import com.bili.diushoujuaner.utils.entity.vo.FriendVo;
import com.bili.diushoujuaner.utils.entity.vo.MemberVo;
import com.bili.diushoujuaner.utils.entity.vo.MessageVo;
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
    private LayoutInflater mInflater;
    private long ownerNo;

    public MessageAdapter(Context context, List<MessageVo> messageVoList, long ownerNo) {
        this.context = context;
        this.messageVoList = messageVoList;
        this.ownerNo = ownerNo;
    }

    public void addFirst(List<MessageVo> messageVoList){
        if(messageVoList == null || messageVoList.isEmpty()){
            return;
        }
        this.messageVoList.addAll(messageVoList);
        notifyDataSetChanged();
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
        setViewStatus(holder, messageVoList.get(position), position);
        return holder.getmConvertView();
    }

    private View getItemLeftView(int position, View convertView, ViewGroup parent){
        ViewHolder holder = ViewHolder.get(context, convertView, parent, R.layout.item_message_left, position);
        setViewStatus(holder, messageVoList.get(position), position);
        return holder.getmConvertView();
    }

    private void setViewStatus(ViewHolder holder, MessageVo messageVo, int position){
        if(messageVo != null){
            //设置是否显示时间
            if(messageVo.isTimeShow()){
                ((TextView)holder.getView(R.id.txtTime)).setText(Common.getFormatTime(messageVo.getTime()));
                holder.getView(R.id.txtTime).setVisibility(View.VISIBLE);
            }else{
                holder.getView(R.id.txtTime).setVisibility(View.GONE);
            }
            //设置昵称是否显示
            if(messageVo.getMsgType() == Constant.CHAT_FRI){
                holder.getView(R.id.txtName).setVisibility(View.GONE);
                FriendVo friendVo = ContactTemper.getFriendVo(messageVo.getFromNo());
                Common.displayDraweeView(friendVo != null ? friendVo.getPicPath() : "", (SimpleDraweeView)holder.getView(R.id.ivHead));
            }else if(messageVo.getMsgType() == Constant.CHAT_PAR){
                holder.getView(R.id.txtName).setVisibility(View.VISIBLE);
                MemberVo memberVo = ContactTemper.getMemberVo(messageVo.getToNo(), messageVo.getFromNo());
                ((TextView)holder.getView(R.id.txtName)).setText(memberVo != null ? memberVo.getMemberName() : "");
                Common.displayDraweeView(memberVo != null ? memberVo.getPicPath() : "", (SimpleDraweeView)holder.getView(R.id.ivHead));
            }
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
            switch (messageVo.getStatus()){
                case Constant.MESSAGE_STATUS_FAIL:
                    holder.getView(R.id.ivStatus).setVisibility(View.VISIBLE);
                    ((ImageView)holder.getView(R.id.ivStatus)).setImageResource(R.mipmap.icon_send_fail);
                    break;
                case Constant.MESSAGE_STATUS_SENDING:
                    holder.getView(R.id.ivStatus).setVisibility(View.VISIBLE);
                    (holder.getView(R.id.ivStatus)).setBackground(ContextCompat.getDrawable(context, R.drawable.spinner));
                    break;
                case Constant.MESSAGE_STATUS_SUCCESS:
                    holder.getView(R.id.ivStatus).setVisibility(View.GONE);
                    break;
            }

        }
    }
}
