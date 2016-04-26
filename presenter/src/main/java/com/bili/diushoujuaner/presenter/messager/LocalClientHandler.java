package com.bili.diushoujuaner.presenter.messager;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.bili.diushoujuaner.model.actionhelper.action.ContactAction;
import com.bili.diushoujuaner.model.databasehelper.DBManager;
import com.bili.diushoujuaner.model.eventhelper.RequestContactEvent;
import com.bili.diushoujuaner.model.eventhelper.DeleteContactEvent;
import com.bili.diushoujuaner.model.eventhelper.ForceOutEvent;
import com.bili.diushoujuaner.model.eventhelper.LoginEvent;
import com.bili.diushoujuaner.model.eventhelper.LoginngEvent;
import com.bili.diushoujuaner.model.eventhelper.LogoutEvent;
import com.bili.diushoujuaner.model.eventhelper.UpdateContactEvent;
import com.bili.diushoujuaner.model.eventhelper.UpdateMessageEvent;
import com.bili.diushoujuaner.model.eventhelper.UpdatePartyEvent;
import com.bili.diushoujuaner.model.eventhelper.UpdateReadCountEvent;
import com.bili.diushoujuaner.model.preferhelper.CustomSessionPreference;
import com.bili.diushoujuaner.model.tempHelper.ChattingTemper;
import com.bili.diushoujuaner.model.tempHelper.ContactTemper;
import com.bili.diushoujuaner.utils.ConstantUtil;
import com.bili.diushoujuaner.utils.entity.vo.MessageVo;
import com.nanotasks.BackgroundWork;
import com.nanotasks.Completion;
import com.nanotasks.Tasks;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by BiLi on 2016/4/20.
 */
public class LocalClientHandler extends Handler {

    private Context context;

    public LocalClientHandler(Context context){
        this.context = context;
    }

    @Override
    public void handleMessage(Message msg) {
        Bundle bundle = msg.getData();
        bundle.setClassLoader(MessageVo.class.getClassLoader());
        MessageVo messageVo;
        switch(msg.what){
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
                processChatMessage((MessageVo)bundle.getParcelable("MessageVo"));
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
                messageVo = bundle.getParcelable("MessageVo");
                if(messageVo == null){
                    return;
                }
                ContactAction.getInstance(context).getAddContact(messageVo.getFromNo(), ConstantUtil.CONTACT_INFO_ADD_BEFORE);
                break;
            case ConstantUtil.HANDLER_PARTY_APPLY:
                EventBus.getDefault().post(new RequestContactEvent());
                break;
            case ConstantUtil.HANDLER_FRIEND_APPLY_AGREE:
                 messageVo = bundle.getParcelable("MessageVo");
                    if(messageVo == null){
                        return;
                }
                ContactAction.getInstance(context).getAddContact(messageVo.getFromNo(), ConstantUtil.CONTACT_INFO_ADD_AFTER);
                break;
            case ConstantUtil.HANDLER_PARTY_APPLY_AGREE:
                messageVo = bundle.getParcelable("MessageVo");
                if(messageVo == null){
                    return;
                }
                if(messageVo.getFromNo() == CustomSessionPreference.getInstance().getCustomSession().getUserNo()){
                    // 是自己，全量获取该群的所有信息，存入本地，通知更新界面
                    ContactAction.getInstance(context).getWholePartyInfo(messageVo.getToNo(), messageVo.getFromNo(), messageVo.getTime());
                }else{
                    // 不是自己，那么已经是该群的成员，只需要添加单个人的信息到数据库，通知更新界面
                    ContactAction.getInstance(context).getSingleMemberInfo(messageVo.getToNo(), messageVo.getFromNo(), messageVo.getTime());
                }
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
        }
    }

    private void processChatMessage(final MessageVo messageVo){
        Tasks.executeInBackground(context, new BackgroundWork<MessageVo>() {
            @Override
            public MessageVo doInBackground() throws Exception {
                ChattingTemper.getInstance().addChattingVoFromServer(messageVo);
                DBManager.getInstance().saveMessage(messageVo);
                return messageVo;
            }
        }, new Completion<MessageVo>() {
            @Override
            public void onSuccess(Context context, MessageVo result) {
                EventBus.getDefault().post(new UpdateMessageEvent(result, UpdateMessageEvent.MESSAGE_RECEIVE));
            }

            @Override
            public void onError(Context context, Exception e) {
            }
        });
    }

}