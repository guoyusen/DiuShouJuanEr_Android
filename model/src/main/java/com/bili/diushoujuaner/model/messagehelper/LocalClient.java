package com.bili.diushoujuaner.model.messagehelper;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import com.bili.diushoujuaner.utils.ConstantUtil;
import com.bili.diushoujuaner.utils.entity.vo.MessageVo;

/**
 * Created by BiLi on 2016/4/20.
 */
public class LocalClient {

    private static LocalClient localClient = null;
    private Messenger serverMessenger;
    private Messenger localMessenger;
    private boolean isBind = false;
    private Context context;
    private ServiceConnection serviceConnection;

    public LocalClient(Context context) {
        this.context = context;
        this.localMessenger = new Messenger(new LocalClientHandler());
        this.serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                serverMessenger = new Messenger(service);
                initLocalServer();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                serverMessenger = null;
            }
        };
    }

    private void initLocalServer(){
        Message msg = Message.obtain(null, ConstantUtil.HANDLER_INIT);
        msg.replyTo = localMessenger;
        try {
            serverMessenger.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void sendMessageToService(int type, MessageVo messageVo){
        Message msg = Message.obtain(null, type);
        Bundle bundle = new Bundle();
        bundle.putParcelable("MessageVo", messageVo);
        msg.setData(bundle);
        try{
            serverMessenger.send(msg);
        }catch(RemoteException e){
            e.printStackTrace();
        }
    }

    public static LocalClient getInstance(Context context){
        if(localClient == null){
            localClient = new LocalClient(context);
        }
        return localClient;
    }

    public void bindLocalServer(Class clzss){
        isBind = context.bindService(new Intent(context, clzss), serviceConnection, android.content.Context.BIND_AUTO_CREATE);
    }

    public void unBindLocalServer(){
        if(this.isBind){
            context.unbindService(serviceConnection);
            dispose();
        }
    }

    public void dispose(){
        localClient = null;
        serviceConnection = null;
        serverMessenger = null;
        localMessenger = null;
    }

}
