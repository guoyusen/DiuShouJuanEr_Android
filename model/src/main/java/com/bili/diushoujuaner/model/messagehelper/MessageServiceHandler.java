package com.bili.diushoujuaner.model.messagehelper;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.widget.Toast;

import com.bili.diushoujuaner.model.preferhelper.ConnectionPreference;
import com.bili.diushoujuaner.utils.ConstantUtil;
import com.bili.diushoujuaner.utils.entity.vo.MessageVo;

/**
 * Created by BiLi on 2016/4/20.
 */
public class MessageServiceHandler extends Handler {

    private Messenger localMessenger;
    private Context ctx;

    private static MessageServiceHandler messageServiceHandler;

    public MessageServiceHandler(Context context){
        messageServiceHandler = this;
        ctx = context;
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
            case ConstantUtil.HANDLER_INIT:
                localMessenger = msg.replyTo;
                if(!ConnectionPreference.getInstance().isConnected() || ConnectionPreference.getInstance().isOverTime()){
                    // 没有连接成功，或者超时，重新连接
                    MinaClient.getInstance(ctx).connect();
                }
                break;
            case ConstantUtil.HANDLER_CHAT:
                Bundle bundle = msg.getData();
                bundle.setClassLoader(MessageVo.class.getClassLoader());
                Transceiver.getInstance().addSendTask((MessageVo)bundle.getParcelable("MessageVo"));
                break;
            case ConstantUtil.HANDLER_LOGOUT:
                localMessenger = null;
                MinaClient.getInstance(ctx).disConnect();
                break;
            case ConstantUtil.HANDLER_RELOGIN:
                MinaClient.getInstance(ctx).connect();
                break;
            case ConstantUtil.HANDLER_NOTICE_CLEAR:
                Notifier.clear();
                break;
            case ConstantUtil.HANDLER_BACKGROUND:
                localMessenger = null;
                break;
            case ConstantUtil.HANDLER_RECONNECTION:
                if(!ConnectionPreference.getInstance().isConnected() || ConnectionPreference.getInstance().isOverTime()){
                    MinaClient.getInstance(ctx).activeConnect();
                }
                break;
        }
    }

    public void sendMessageToClient(int type, MessageVo messageVo){
        Message msg = Message.obtain(null, type);
        Bundle bundle = new Bundle();
        bundle.putParcelable("MessageVo", messageVo);
        msg.setData(bundle);
        try{
            if(localMessenger != null){
                localMessenger.send(msg);
            }
        }catch(RemoteException e){
            e.printStackTrace();
        }
    }
}
