package com.bili.diushoujuaner.presenter.messager;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import com.bili.diushoujuaner.utils.Constant;
import com.bili.diushoujuaner.utils.entity.vo.MessageVo;

/**
 * Created by BiLi on 2016/4/20.
 */
public class MessageServiceHandler extends Handler {

    private Messenger localMessager;

    private static MessageServiceHandler messageServiceHandler;

    public MessageServiceHandler(){
        messageServiceHandler = this;
    }

    public static MessageServiceHandler getInstance(){
        if(messageServiceHandler == null){
            throw new RuntimeException("MessageServiceHandler 没有初始化");
        }
        return messageServiceHandler;
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what){
            case Constant.HANDLER_INIT:
                Log.d("guoyusenmm","初始化。。。");
                localMessager = msg.replyTo;
                MinaClienter.getInstance().connect();
                break;
            case Constant.HANDLER_CHAT:
                Bundle bundle = msg.getData();
                bundle.setClassLoader(MessageVo.class.getClassLoader());
                Transceiver.getInstance().addSendTask((MessageVo)bundle.getParcelable("MessageVo"));
                break;
            case Constant.HANDLER_LOGOUT:
                localMessager = null;
                MinaClienter.getInstance().disConnect();
                break;
            case Constant.HANDLER_RELOGIN:
                MinaClienter.getInstance().connect();
                break;
        }
    }

    public void sendMessageToClient(int type, MessageVo messageVo){
        Message msg = Message.obtain(null, type);
        Bundle bundle = new Bundle();
        bundle.putParcelable("MessageVo", messageVo);
        msg.setData(bundle);
        try{
            if(localMessager != null){
                localMessager.send(msg);
            }
        }catch(RemoteException e){
            e.printStackTrace();
        }
    }
}
