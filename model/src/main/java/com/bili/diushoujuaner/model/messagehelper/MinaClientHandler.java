package com.bili.diushoujuaner.model.messagehelper;

import android.content.Context;
import android.util.Log;

import com.bili.diushoujuaner.model.actionhelper.action.ContactAction;
import com.bili.diushoujuaner.model.databasehelper.DBManager;
import com.bili.diushoujuaner.model.preferhelper.ConnectionPreference;
import com.bili.diushoujuaner.model.preferhelper.CustomSessionPreference;
import com.bili.diushoujuaner.utils.CommonUtil;
import com.bili.diushoujuaner.utils.ConstantUtil;
import com.bili.diushoujuaner.utils.EntityUtil;
import com.bili.diushoujuaner.utils.GsonUtil;
import com.bili.diushoujuaner.utils.NoticeUtil;
import com.bili.diushoujuaner.utils.TimeUtil;
import com.bili.diushoujuaner.utils.entity.dto.ContactDto;
import com.bili.diushoujuaner.utils.entity.vo.MessageVo;
import com.google.gson.reflect.TypeToken;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

/**
 * Created by BiLi on 2016/4/15.
 */
public class MinaClientHandler extends IoHandlerAdapter {

    private Context context;

    public MinaClientHandler(Context context) {
        this.context = context;
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        ConnectionPreference.getInstance().saveState(false);
        MessageServiceHandler.getInstance().sendMessageToClient(ConstantUtil.HANDLER_LOGOUT, null);
        Log.d("guoyusenm","通信异常");
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        MessageVo messageVo = EntityUtil.getMessageVoFromReceive(message.toString());
        if(messageVo == null){
            return;
        }
        switch (messageVo.getMsgType()){
            case ConstantUtil.CHAT_FRI:
                //好友消息通知
                Notifier.showNotification(messageVo);
                processMessage(messageVo, session);
                break;
            case ConstantUtil.CHAT_PAR:
                //群消息通知
                Notifier.showNotification(messageVo);
                processMessage(messageVo, session);
                break;
            case ConstantUtil.CHAT_GOOD:
                break;
            case ConstantUtil.CHAT_STATUS:
                Transceiver.getInstance().updateStatusSuccess(messageVo.getSerialNo());
                break;
            case ConstantUtil.CHAT_CLOSE:
                MessageServiceHandler.getInstance().sendMessageToClient(ConstantUtil.HANDLER_CLOSE, messageVo);
                break;
            case ConstantUtil.CHAT_PARTY_HEAD:
                session.write(EntityUtil.getEmptyMessage(messageVo.getSerialNo(), -1, ConstantUtil.CHAT_STATUS));
                DBManager.getInstance().updatePartyHeadPic(messageVo.getToNo(), messageVo.getContent());
                MessageServiceHandler.getInstance().sendMessageToClient(ConstantUtil.HANDLER_PARTY_HEAD, messageVo);
                break;
            case ConstantUtil.CHAT_PARTY_NAME:
                session.write(EntityUtil.getEmptyMessage(messageVo.getSerialNo(), -1, ConstantUtil.CHAT_STATUS));
                DBManager.getInstance().updatePartyName(messageVo.getToNo(), messageVo.getContent());
                MessageServiceHandler.getInstance().sendMessageToClient(ConstantUtil.HANDLER_PARTY_NAME, messageVo);
                break;
            case ConstantUtil.CHAT_PARTY_MEMBER_NAME:
                session.write(EntityUtil.getEmptyMessage(messageVo.getSerialNo(), -1, ConstantUtil.CHAT_STATUS));
                DBManager.getInstance().updateMemberName(messageVo.getToNo(), messageVo.getFromNo(), messageVo.getContent());
                MessageServiceHandler.getInstance().sendMessageToClient(ConstantUtil.HANDLER_PARTY_MEMBER_NAME, messageVo);
                break;
            case ConstantUtil.CHAT_FRIEND_APPLY:
                session.write(EntityUtil.getEmptyMessage(messageVo.getSerialNo(), -1, ConstantUtil.CHAT_STATUS));
                NoticeUtil.getInstance().playNotice();
                DBManager.getInstance().saveApply(messageVo.getFromNo(), messageVo.getToNo(),messageVo.getContent(), messageVo.getTime(), ConstantUtil.CHAT_FRIEND_APPLY);
                ContactAction.getInstance(context).getAddContact(messageVo.getFromNo(), ConstantUtil.CONTACT_FRIEND_APPLY_INFO_BEFORE, false);
                break;
            case ConstantUtil.CHAT_FRIEND_RECOMMEND:
                session.write(EntityUtil.getEmptyMessage(messageVo.getSerialNo(), -1, ConstantUtil.CHAT_STATUS));
                NoticeUtil.getInstance().playNotice();
                DBManager.getInstance().saveApply(messageVo.getFromNo(), messageVo.getToNo(),messageVo.getContent(), messageVo.getTime(), ConstantUtil.CHAT_FRIEND_RECOMMEND);
                ContactAction.getInstance(context).getAddContact(messageVo.getFromNo(), ConstantUtil.CONTACT_FRIEND_APPLY_INFO_BEFORE, false);
                break;
            case ConstantUtil.CHAT_PARTY_APPLY:
                session.write(EntityUtil.getEmptyMessage(messageVo.getSerialNo(), -1, ConstantUtil.CHAT_STATUS));
                NoticeUtil.getInstance().playNotice();
                DBManager.getInstance().saveApply(messageVo.getFromNo(), Long.valueOf(messageVo.getTime()),messageVo.getContent(), TimeUtil.getCurrentTimeYYMMDD_HHMMSS(), ConstantUtil.CHAT_PARTY_APPLY);
                ContactAction.getInstance(context).getAddContact(messageVo.getFromNo(), ConstantUtil.CONTACT_PARTY_APPLY_INFO, false);
                break;
            case ConstantUtil.CHAT_FRIEND_DELETE:
                session.write(EntityUtil.getEmptyMessage(messageVo.getSerialNo(), -1, ConstantUtil.CHAT_STATUS));
                DBManager.getInstance().deleteFriend(messageVo.getFromNo());
                MessageServiceHandler.getInstance().sendMessageToClient(ConstantUtil.HANDLER_FRIEND_DELETE, messageVo);
                break;
            case ConstantUtil.CHAT_FRIEND_APPLY_AGREE:
                ContactAction.getInstance(context).getAddContact(messageVo.getFromNo(), ConstantUtil.CONTACT_FRIEND_APPLY_INFO_AFTER, false);
                messageVo.setMsgType(ConstantUtil.CHAT_FRI);
                processMessage(messageVo, session);
                break;
            case ConstantUtil.CHAT_PARTY_APPLY_AGREE:
                session.write(EntityUtil.getEmptyMessage(messageVo.getSerialNo(), -1, ConstantUtil.CHAT_STATUS));
                if(messageVo.getFromNo() == CustomSessionPreference.getInstance().getCustomSession().getUserNo()){
                    // 是自己，全量获取该群的所有信息，存入本地，通知更新界面
                    ContactAction.getInstance(context).getWholePartyInfo(messageVo.getToNo(), messageVo.getFromNo(), messageVo.getTime(), false);
                }else{
                    // 不是自己，那么已经是该群的成员，只需要添加单个人的信息到数据库，通知更新界面
                    ContactAction.getInstance(context).getSingleMemberInfo(messageVo.getToNo(), messageVo.getFromNo(), messageVo.getTime(), false);
                }
                break;
            case ConstantUtil.CHAT_PARTY_MEMBER_EXIT:
                session.write(EntityUtil.getEmptyMessage(messageVo.getSerialNo(), -1, ConstantUtil.CHAT_STATUS));
                if(messageVo.getFromNo() == CustomSessionPreference.getInstance().getCustomSession().getUserNo()){
                    //被群主踢除
                    DBManager.getInstance().deleteParty(messageVo.getToNo());
                }else{
                    DBManager.getInstance().deleteMember(messageVo.getToNo(), messageVo.getFromNo());
                    DBManager.getInstance().deletePartyChat(messageVo.getToNo(), messageVo.getFromNo());
                }
                MessageServiceHandler.getInstance().sendMessageToClient(ConstantUtil.HANDLER_PARTY_MEMBER_EXIT, messageVo);
                break;
            case ConstantUtil.CHAT_MEMBER_BATCH_ADD:
                session.write(EntityUtil.getEmptyMessage(messageVo.getSerialNo(), -1, ConstantUtil.CHAT_STATUS));
                ContactDto contactDto = GsonUtil.getInstance().fromJson(messageVo.getContent(), new TypeToken<ContactDto>(){}.getType());
                ContactAction.getInstance(context).saveNewMembersInfo(contactDto, messageVo.getToNo(), messageVo.getFromNo(), false);
                break;
            case ConstantUtil.CHAT_PARTY_UNGROUP:
                session.write(EntityUtil.getEmptyMessage(messageVo.getSerialNo(), -1, ConstantUtil.CHAT_STATUS));
                DBManager.getInstance().deleteParty(Long.valueOf(messageVo.getContent()));
                MessageServiceHandler.getInstance().sendMessageToClient(ConstantUtil.HANDLER_PARTY_UNGROUP, messageVo);
                break;
        }
    }

    private void processMessage(MessageVo messageVo, IoSession session){
        session.write(EntityUtil.getEmptyMessage(messageVo.getSerialNo(), -1, ConstantUtil.CHAT_STATUS));
        if(messageVo.getMsgType() == ConstantUtil.CHAT_FRI){
            DBManager.getInstance().updateFriendRecent(messageVo.getFromNo(), true);
        }else if(messageVo.getMsgType() == ConstantUtil.CHAT_PAR){
            DBManager.getInstance().updateMemberRecent(messageVo.getToNo(), true);
        }
        messageVo.setContent(CommonUtil.htmlEscapeCharsToString(messageVo.getContent()));
        DBManager.getInstance().saveMessage(messageVo);
        NoticeUtil.getInstance().playNotice();
        MessageServiceHandler.getInstance().sendMessageToClient(ConstantUtil.HANDLER_CHAT, messageVo);
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        Log.d("guoyusenm","通信关闭");
        ConnectionPreference.getInstance().saveState(false);
        MessageServiceHandler.getInstance().sendMessageToClient(ConstantUtil.HANDLER_LOGOUT, null);
    }

    @Override
    public void sessionCreated(IoSession session) throws Exception {
        //创建session后，发送该客户端的账号，在服务端进行注册session在线
        ConnectionPreference.getInstance().saveState(true);
        MessageServiceHandler.getInstance().sendMessageToClient(ConstantUtil.HANDLER_LOGINING, null);
        Transceiver.getInstance().addSendTask(EntityUtil.getEmptyMessageVo(CustomSessionPreference.getInstance().getCustomSession().getUserNo(), ConstantUtil.CHAT_INIT));
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        ConnectionPreference.getInstance().saveState(false);
        MinaClient.getInstance(context).disConnect();
        MinaClient.getInstance(context).reConnect();
    }

}
