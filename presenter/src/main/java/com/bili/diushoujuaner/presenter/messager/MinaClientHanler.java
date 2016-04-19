package com.bili.diushoujuaner.presenter.messager;

import android.util.Log;

import com.bili.diushoujuaner.model.databasehelper.DBManager;
import com.bili.diushoujuaner.model.eventhelper.UpdateMessageEvent;
import com.bili.diushoujuaner.model.preferhelper.CustomSessionPreference;
import com.bili.diushoujuaner.model.tempHelper.ChattingTemper;
import com.bili.diushoujuaner.utils.Common;
import com.bili.diushoujuaner.utils.Constant;
import com.bili.diushoujuaner.utils.MessageNoticer;
import com.bili.diushoujuaner.utils.entity.dto.MessageDto;
import com.bili.diushoujuaner.utils.entity.vo.MessageVo;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.greenrobot.eventbus.EventBus;

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
        MessageDto messageDto = Common.getMessageDtoFromJSONString(message.toString());
        switch (messageDto.getMsgType()){
            case Constant.CHAT_FRI:
            case Constant.CHAT_PAR:
                //给服务端发送已接收状态
                session.write(Common.getEmptyMessage(messageDto.getSerialNo(), -1, Constant.CHAT_STATUS));
                messageDto.setMsgContent(Common.htmlEscapeCharsToString(messageDto.getMsgContent()));
                MessageVo messageVo = Common.getMessageVoFromReceive(message.toString());
                ChattingTemper.getInstance().addChattingVoNew(messageVo);
                DBManager.getInstance().saveMessage(messageVo);
                EventBus.getDefault().post(new UpdateMessageEvent(messageVo, UpdateMessageEvent.MESSAGE_RECEIVE));
                MessageNoticer.getInstance().playNotice();
                break;
            case Constant.CHAT_GOOD:

                break;
            case Constant.CHAT_STATUS:
                //通过序列号更新状态
                Log.d("guoyusens","收到发送成功");
                Transceiver.getInstance().updateStatusSuccess(messageDto.getSerialNo());
                break;
        }
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        Log.d("guoyusenm","通信关闭");
    }

    @Override
    public void sessionCreated(IoSession session) throws Exception {
        //创建session后，发送该客户端的账号，在服务端进行注册session在线
        Transceiver.getInstance().addSendTask(Common.getEmptyMessageVo(CustomSessionPreference.getInstance().getCustomSession().getUserNo(), Constant.CHAT_INIT));
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        Log.d("guoyusenm","未在规定时间内收到心跳请求包");
    }

}
