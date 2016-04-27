package com.bili.diushoujuaner.presenter.messager;

import android.util.Log;

import com.bili.diushoujuaner.model.databasehelper.DBManager;
import com.bili.diushoujuaner.model.preferhelper.CustomSessionPreference;
import com.bili.diushoujuaner.utils.CommonUtil;
import com.bili.diushoujuaner.utils.ConstantUtil;
import com.bili.diushoujuaner.utils.EntityUtil;
import com.bili.diushoujuaner.utils.NoticeUtil;
import com.bili.diushoujuaner.utils.TimeUtil;
import com.bili.diushoujuaner.utils.entity.vo.MessageVo;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

/**
 * Created by BiLi on 2016/4/15.
 */
public class MinaClientHanler extends IoHandlerAdapter {

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        cause.printStackTrace();
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
                processMessage(messageVo, session);
                break;
            case ConstantUtil.CHAT_PAR:
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
                NoticeUtil.getInstance().playNotice();
                session.write(EntityUtil.getEmptyMessage(messageVo.getSerialNo(), -1, ConstantUtil.CHAT_STATUS));
                DBManager.getInstance().saveApply(messageVo.getFromNo(), messageVo.getToNo(),messageVo.getContent(), messageVo.getTime(), ConstantUtil.CHAT_FRIEND_APPLY);
                MessageServiceHandler.getInstance().sendMessageToClient(ConstantUtil.HANDLER_FRIEND_APPLY, messageVo);
                break;
            case ConstantUtil.CHAT_PARTY_APPLY:
                session.write(EntityUtil.getEmptyMessage(messageVo.getSerialNo(), -1, ConstantUtil.CHAT_STATUS));
                DBManager.getInstance().saveApply(messageVo.getFromNo(), Long.valueOf(messageVo.getTime()),messageVo.getContent(), TimeUtil.getCurrentTimeYYMMDD_HHMMSS(), ConstantUtil.CHAT_PARTY_APPLY);
                MessageServiceHandler.getInstance().sendMessageToClient(ConstantUtil.HANDLER_PARTY_APPLY, null);
                break;
            case ConstantUtil.CHAT_FRIEND_DELETE:
                session.write(EntityUtil.getEmptyMessage(messageVo.getSerialNo(), -1, ConstantUtil.CHAT_STATUS));
                DBManager.getInstance().deleteFriend(messageVo.getFromNo());
                MessageServiceHandler.getInstance().sendMessageToClient(ConstantUtil.HANDLER_FRIEND_DELETE, messageVo);
                break;
            case ConstantUtil.CHAT_FRIEND_APPLY_AGREE:
                if(!DBManager.getInstance().isFriended(messageVo.getFromNo())){
                    MessageServiceHandler.getInstance().sendMessageToClient(ConstantUtil.HANDLER_FRIEND_APPLY_AGREE, messageVo);
                }
                messageVo.setMsgType(ConstantUtil.CHAT_FRI);
                processMessage(messageVo, session);
                break;
            case ConstantUtil.CHAT_PARTY_APPLY_AGREE:
                session.write(EntityUtil.getEmptyMessage(messageVo.getSerialNo(), -1, ConstantUtil.CHAT_STATUS));
                MessageServiceHandler.getInstance().sendMessageToClient(ConstantUtil.HANDLER_PARTY_APPLY_AGREE, messageVo);
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
        }
    }

    private void processMessage(MessageVo messageVo, IoSession session){
        if(messageVo.getMsgType() == ConstantUtil.CHAT_FRI){
            DBManager.getInstance().updateFriendRecent(messageVo.getFromNo(), true);
        }else if(messageVo.getMsgType() == ConstantUtil.CHAT_PAR){
            DBManager.getInstance().updateMemberRecent(messageVo.getToNo(), true);
        }
        session.write(EntityUtil.getEmptyMessage(messageVo.getSerialNo(), -1, ConstantUtil.CHAT_STATUS));
        messageVo.setContent(CommonUtil.htmlEscapeCharsToString(messageVo.getContent()));
        NoticeUtil.getInstance().playNotice();
        MessageServiceHandler.getInstance().sendMessageToClient(ConstantUtil.HANDLER_CHAT, messageVo);
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        Log.d("guoyusenm","通信关闭");
        MessageServiceHandler.getInstance().sendMessageToClient(ConstantUtil.HANDLER_LOGOUT, null);
    }

    @Override
    public void sessionCreated(IoSession session) throws Exception {
        //创建session后，发送该客户端的账号，在服务端进行注册session在线
        MessageServiceHandler.getInstance().sendMessageToClient(ConstantUtil.HANDLER_LOGINING, null);
        Transceiver.getInstance().addSendTask(EntityUtil.getEmptyMessageVo(CustomSessionPreference.getInstance().getCustomSession().getUserNo(), ConstantUtil.CHAT_INIT));
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        MinaClienter.getInstance().disConnect();
        MinaClienter.getInstance().reConnect();
    }

}
