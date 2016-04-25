package com.bili.diushoujuaner.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bili.diushoujuaner.R;
import com.bili.diushoujuaner.adapter.viewholder.ViewHolder;
import com.bili.diushoujuaner.callback.OnChatReadDismissListener;
import com.bili.diushoujuaner.model.tempHelper.ContactTemper;
import com.bili.diushoujuaner.utils.CommonUtil;
import com.bili.diushoujuaner.utils.ConstantUtil;
import com.bili.diushoujuaner.utils.TimeUtil;
import com.bili.diushoujuaner.utils.entity.vo.ChattingVo;
import com.bili.diushoujuaner.utils.entity.vo.FriendVo;
import com.bili.diushoujuaner.utils.entity.vo.MemberVo;
import com.bili.diushoujuaner.utils.entity.vo.PartyVo;
import com.bili.diushoujuaner.widget.badgeview.BGABadgeRelativeLayout;
import com.bili.diushoujuaner.widget.badgeview.BGADragDismissDelegate;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by BiLi on 2016/3/5.
 */
public class ChattingAdapter extends CommonAdapter<ChattingVo> {

    private StringBuilder content = new StringBuilder();
    private OnChatReadDismissListener onChatReadDismissListener;

    public ChattingAdapter(Context context, List<ChattingVo> list){
        super(context,list, R.layout.item_main_chatting);
    }

    public void setOnChatReadDismissListener(OnChatReadDismissListener onChatReadDismissListener) {
        this.onChatReadDismissListener = onChatReadDismissListener;
    }

    @Override
    public void convert(ViewHolder holder, ChattingVo chattingVo, final int position) throws Exception {
        if(chattingVo != null){
            //设置头像，昵称
            if(chattingVo.getMsgType() == ConstantUtil.CHAT_FRI){
                FriendVo friendVo = ContactTemper.getInstance().getFriendVo(chattingVo.getUserNo());
                if(friendVo != null){
                    ((TextView)holder.getView(R.id.txtName)).setText(ContactTemper.getInstance().getFriendVo(chattingVo.getUserNo()).getDisplayName());
                    CommonUtil.displayDraweeView(ContactTemper.getInstance().getFriendVo(chattingVo.getUserNo()).getPicPath(),(SimpleDraweeView)holder.getView(R.id.ivHead));
                }else{
                    ((TextView)holder.getView(R.id.txtName)).setText(chattingVo.getUserNo() + "");
                    CommonUtil.displayDraweeView("",(SimpleDraweeView)holder.getView(R.id.ivHead));
                }
            }else if(chattingVo.getMsgType() == ConstantUtil.CHAT_PAR){
                PartyVo partyVo = ContactTemper.getInstance().getPartyVo(chattingVo.getUserNo());
                if(partyVo != null){
                    ((TextView)holder.getView(R.id.txtName)).setText(partyVo.getDisplayName());
                    CommonUtil.displayDraweeView(partyVo.getPicPath(),(SimpleDraweeView)holder.getView(R.id.ivHead));
                }else{
                    ((TextView)holder.getView(R.id.txtName)).setText(chattingVo.getUserNo() + "");
                    CommonUtil.displayDraweeView("",(SimpleDraweeView)holder.getView(R.id.ivHead));
                }
            }
            //设置红点提示
            if(chattingVo.getUnReadCount() <= 0){
                ((BGABadgeRelativeLayout)holder.getView(R.id.layoutBadge)).hiddenBadge();
            }else{
                if(chattingVo.getMsgType() == ConstantUtil.CHAT_PAR){
                    ((BGABadgeRelativeLayout)holder.getView(R.id.layoutBadge)).setBadgeBgColor(Color.parseColor("#96d7f0"));
                }else if(chattingVo.getMsgType() == ConstantUtil.CHAT_FRI){
                    ((BGABadgeRelativeLayout)holder.getView(R.id.layoutBadge)).setBadgeBgColor(Color.RED);
                }
                ((BGABadgeRelativeLayout)holder.getView(R.id.layoutBadge)).showTextBadge(chattingVo.getUnReadCount() + "");
            }
            ((BGABadgeRelativeLayout) holder.getView(R.id.layoutBadge)).setDragDismissDelegage(new BGADragDismissDelegate() {
                @Override
                public void onDismiss() {
                    if(onChatReadDismissListener == null){
                        return;
                    }
                    onChatReadDismissListener.onChatReadDismiss(position);
                }
            });
            //设置内容显示

            if(chattingVo.getMsgType() == ConstantUtil.CHAT_PAR){
                MemberVo memberVo = ContactTemper.getInstance().getMemberVo(chattingVo.getUserNo(), chattingVo.getMemberNo());
                if(memberVo != null){
                    content.append("[" + memberVo.getMemberName() + "]:");
                }
            }
            switch (chattingVo.getConType()){
                case ConstantUtil.CHAT_CONTENT_FRIEND_AGREE:
                case ConstantUtil.CHAT_CONTENT_TEXT:
                    content.append(chattingVo.getContent());
                    break;
                case ConstantUtil.CHAT_CONTENT_IMG:
                    content.append("[图片]");
                    break;
                case ConstantUtil.CHAT_CONTENT_VOICE:
                    content.append("[语音]");
                    break;
            }
            ((TextView)holder.getView(R.id.txtContent)).setText(content.toString());
            content.delete(0, content.length());
            //设置时间
            ((TextView)holder.getView(R.id.txtTime)).setText(TimeUtil.getFormatTime(chattingVo.getTime()));

            //设置状态图标
            switch (chattingVo.getStatus()){
                case ConstantUtil.MESSAGE_STATUS_SUCCESS:
                    holder.getView(R.id.ivStatus).setVisibility(View.GONE);
                    break;
                case ConstantUtil.MESSAGE_STATUS_SENDING:
                    holder.getView(R.id.ivStatus).setVisibility(View.VISIBLE);
                    ((ImageView)holder.getView(R.id.ivStatus)).setImageResource(R.mipmap.icon_chat_sending);
                    break;
                case ConstantUtil.MESSAGE_STATUS_FAIL:
                    holder.getView(R.id.ivStatus).setVisibility(View.VISIBLE);
                    ((ImageView)holder.getView(R.id.ivStatus)).setImageResource(R.mipmap.icon_message_fail);
                    break;
            }

        }
    }
}
