package com.bili.diushoujuaner.presenter.messager;

import android.util.Log;

import com.bili.diushoujuaner.model.databasehelper.DBManager;
import com.bili.diushoujuaner.model.preferhelper.CustomSessionPreference;
import com.bili.diushoujuaner.utils.Common;
import com.bili.diushoujuaner.utils.Constant;
import com.bili.diushoujuaner.utils.MessageNoticer;
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
        MessageVo messageVo = Common.getMessageVoFromReceive(message.toString());
        if(messageVo == null){
            return;
        }
        switch (messageVo.getMsgType()){
            case Constant.CHAT_FRI:
            case Constant.CHAT_PAR:
                if(messageVo.getMsgType() == Constant.CHAT_FRI){
                    DBManager.getInstance().updateFriendRecent(messageVo.getFromNo(), true);
                }else if(messageVo.getMsgType() == Constant.CHAT_PAR){
                    DBManager.getInstance().updateMemberRecent(messageVo.getToNo(), true);
                }
                session.write(Common.getEmptyMessage(messageVo.getSerialNo(), -1, Constant.CHAT_STATUS));
                messageVo.setContent(Common.htmlEscapeCharsToString(messageVo.getContent()));
                MessageNoticer.getInstance().playNotice();
                MessageServiceHandler.getInstance().sendMessageToClient(Constant.HANDLER_CHAT, messageVo);
                break;
            case Constant.CHAT_GOOD:
                break;
            case Constant.CHAT_STATUS:
                Transceiver.getInstance().updateStatusSuccess(messageVo.getSerialNo());
                break;
            case Constant.CHAT_CLOSE:
                MessageServiceHandler.getInstance().sendMessageToClient(Constant.HANDLER_CLOSE, messageVo);
                break;
            case Constant.CHAT_PARTY_HEAD:
                session.write(Common.getEmptyMessage(messageVo.getSerialNo(), -1, Constant.CHAT_STATUS));
                DBManager.getInstance().updatePartyHeadPic(messageVo.getToNo(), messageVo.getContent());
                MessageServiceHandler.getInstance().sendMessageToClient(Constant.HANDLER_PARTY_HEAD, messageVo);
                break;
            case Constant.CHAT_PARTY_NAME:
                session.write(Common.getEmptyMessage(messageVo.getSerialNo(), -1, Constant.CHAT_STATUS));
                DBManager.getInstance().updatePartyName(messageVo.getToNo(), messageVo.getContent());
                MessageServiceHandler.getInstance().sendMessageToClient(Constant.HANDLER_PARTY_NAME, messageVo);
                break;
            case Constant.CHAT_PARTY_MEMBER_NAME:
                session.write(Common.getEmptyMessage(messageVo.getSerialNo(), -1, Constant.CHAT_STATUS));
                DBManager.getInstance().updateMemberName(messageVo.getToNo(), messageVo.getFromNo(), messageVo.getContent());
                MessageServiceHandler.getInstance().sendMessageToClient(Constant.HANDLER_PARTY_MEMBER_NAME, messageVo);
                break;
        }
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        Log.d("guoyusenm","通信关闭");
        MessageServiceHandler.getInstance().sendMessageToClient(Constant.HANDLER_LOGOUT, null);
    }

    @Override
    public void sessionCreated(IoSession session) throws Exception {
        //创建session后，发送该客户端的账号，在服务端进行注册session在线
        MessageServiceHandler.getInstance().sendMessageToClient(Constant.HANDLER_LOGINING, null);
        Transceiver.getInstance().addSendTask(Common.getEmptyMessageVo(CustomSessionPreference.getInstance().getCustomSession().getUserNo(), Constant.CHAT_INIT));
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        MinaClienter.getInstance().disConnect();
        MinaClienter.getInstance().reConnect();
    }

}
