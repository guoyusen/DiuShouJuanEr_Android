package com.bili.diushoujuaner.model.messagehelper;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.bili.diushoujuaner.model.eventhelper.DeleteContactEvent;
import com.bili.diushoujuaner.model.eventhelper.ForceOutEvent;
import com.bili.diushoujuaner.model.eventhelper.HeartBeatEvent;
import com.bili.diushoujuaner.model.eventhelper.LoginEvent;
import com.bili.diushoujuaner.model.eventhelper.LoginngEvent;
import com.bili.diushoujuaner.model.eventhelper.LogoutEvent;
import com.bili.diushoujuaner.model.eventhelper.NoticeAddMemberEvent;
import com.bili.diushoujuaner.model.eventhelper.RequestContactEvent;
import com.bili.diushoujuaner.model.eventhelper.UnGroupPartyEvent;
import com.bili.diushoujuaner.model.eventhelper.UpdateContactEvent;
import com.bili.diushoujuaner.model.eventhelper.UpdateMessageEvent;
import com.bili.diushoujuaner.model.eventhelper.UpdatePartyEvent;
import com.bili.diushoujuaner.model.tempHelper.ChattingTemper;
import com.bili.diushoujuaner.model.tempHelper.ContactTemper;
import com.bili.diushoujuaner.utils.ConstantUtil;
import com.bili.diushoujuaner.utils.entity.vo.MessageVo;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by BiLi on 2016/4/20.
 */
public class LocalClientHandler extends Handler {

    @Override
    public void handleMessage(Message msg) {
        Bundle bundle = msg.getData();
        bundle.setClassLoader(MessageVo.class.getClassLoader());
        MessageVo messageVo;
        switch(msg.what){
            case ConstantUtil.HANDLER_HEARTBEAT:
                EventBus.getDefault().post(new HeartBeatEvent());
                break;
            case ConstantUtil.HANDLER_LOGIN:
                EventBus.getDefault().post(new LoginEvent());
                break;
            case ConstantUtil.HANDLER_LOGINING:
                EventBus.getDefault().post(new LoginngEvent());
                break;
            case ConstantUtil.HANDLER_LOGOUT:
                EventBus.getDefault().post(new LogoutEvent());
                break;
            case ConstantUtil.HANDLER_CHAT:
                messageVo = bundle.getParcelable("MessageVo");
                if(messageVo == null){
                    return;
                }
                ChattingTemper.getInstance().addChattingVoFromServer(messageVo);
                EventBus.getDefault().post(new UpdateMessageEvent(messageVo, UpdateMessageEvent.MESSAGE_RECEIVE));
                break;
            case ConstantUtil.HANDLER_STATUS:
                ChattingTemper.getInstance().updateChattingVo((MessageVo) bundle.getParcelable("MessageVo"));
                EventBus.getDefault().post(new UpdateMessageEvent((MessageVo) bundle.getParcelable("MessageVo"), UpdateMessageEvent.MESSAGE_STATUS));
                break;
            case ConstantUtil.HANDLER_CLOSE:
                EventBus.getDefault().post(new ForceOutEvent());
                break;
            case ConstantUtil.HANDLER_PARTY_HEAD:
                messageVo = bundle.getParcelable("MessageVo");
                if(messageVo == null){
                    return;
                }
                ContactTemper.getInstance().updatePartyHeadPic(messageVo.getToNo(), messageVo.getContent());
                EventBus.getDefault().post(new UpdatePartyEvent(messageVo.getToNo(), messageVo.getContent(), ConstantUtil.CHAT_PARTY_HEAD));
                break;
            case ConstantUtil.HANDLER_PARTY_NAME:
                messageVo = bundle.getParcelable("MessageVo");
                if(messageVo == null){
                    return;
                }
                ContactTemper.getInstance().updatePartyName(messageVo.getToNo(), messageVo.getContent());
                EventBus.getDefault().post(new UpdatePartyEvent(messageVo.getToNo(), messageVo.getContent(), ConstantUtil.CHAT_PARTY_NAME));
                break;
            case ConstantUtil.HANDLER_PARTY_MEMBER_NAME:
                messageVo = bundle.getParcelable("MessageVo");
                if(messageVo == null){
                    return;
                }
                ContactTemper.getInstance().updateMemberName(messageVo.getToNo(), messageVo.getFromNo(), messageVo.getContent());
                EventBus.getDefault().post(new UpdatePartyEvent(messageVo.getToNo(), messageVo.getContent(), ConstantUtil.CHAT_PARTY_MEMBER_NAME));
                break;
            case ConstantUtil.HANDLER_FRIEND_APPLY:
                EventBus.getDefault().post(new RequestContactEvent());
                break;
            case ConstantUtil.HANDLER_PARTY_APPLY:
                EventBus.getDefault().post(new RequestContactEvent());
                break;
            case ConstantUtil.HANDLER_FRIEND_APPLY_AGREE:
                EventBus.getDefault().post(new UpdateContactEvent());
                break;
            case ConstantUtil.HANDLER_PARTY_APPLY_AGREE:
                messageVo = bundle.getParcelable("MessageVo");
                if(messageVo == null){
                    return;
                }
                ChattingTemper.getInstance().addChattingVoFromServer(messageVo);
                EventBus.getDefault().post(new UpdateContactEvent());
                EventBus.getDefault().post(new NoticeAddMemberEvent(messageVo));
                break;
            case ConstantUtil.HANDLER_FRIEND_DELETE:
                messageVo = bundle.getParcelable("MessageVo");
                if(messageVo == null){
                    return;
                }
                ChattingTemper.getInstance().deleteChattingVo(messageVo.getFromNo(), ConstantUtil.CHAT_FRI);
                EventBus.getDefault().post(new DeleteContactEvent(ConstantUtil.DELETE_CONTACT_FRIEND, messageVo.getFromNo()));
                break;
            case ConstantUtil.HANDLER_PARTY_MEMBER_EXIT:
                messageVo = bundle.getParcelable("MessageVo");
                if(messageVo == null){
                    return;
                }
                EventBus.getDefault().post(new UpdateContactEvent());
                EventBus.getDefault().post(new UpdatePartyEvent(messageVo.getToNo(),messageVo.getFromNo(), "", ConstantUtil.CHAT_PARTY_MEMBER_EXIT));
                break;
            case ConstantUtil.HANDLER_MEMBER_BATCH_ADD:
                messageVo = bundle.getParcelable("MessageVo");
                if(messageVo == null){
                    return;
                }
                ChattingTemper.getInstance().addChattingVoFromServer(messageVo);
                EventBus.getDefault().post(new UpdateContactEvent());
                EventBus.getDefault().post(new NoticeAddMemberEvent(messageVo));
                break;

            case ConstantUtil.HANDLER_PARTY_UNGROUP:
                messageVo = bundle.getParcelable("MessageVo");
                if(messageVo == null){
                    return;
                }
                ChattingTemper.getInstance().deleteChattingVo(Long.valueOf(messageVo.getContent()), ConstantUtil.CHAT_PAR);
                EventBus.getDefault().post(new UpdateContactEvent());
                EventBus.getDefault().post(new UnGroupPartyEvent(Long.valueOf(messageVo.getContent())));
                break;
        }
    }

}