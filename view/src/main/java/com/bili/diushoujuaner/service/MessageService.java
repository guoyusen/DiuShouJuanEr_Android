package com.bili.diushoujuaner.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Messenger;
import android.support.annotation.Nullable;

import com.bili.diushoujuaner.model.eventhelper.LoginConflictEvent;
import com.bili.diushoujuaner.presenter.messager.MessageServiceHandler;
import com.bili.diushoujuaner.presenter.messager.MinaClienter;
import com.bili.diushoujuaner.presenter.messager.Transceiver;
import com.bili.diushoujuaner.utils.Constant;
import com.bili.diushoujuaner.widget.dialog.DialogTool;
import com.bili.diushoujuaner.widget.dialog.OnBothClickListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by BiLi on 2016/4/15.
 */
public class MessageService extends Service {

    public static final String TAG = "MessageService";
    private Messenger serverMessager;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return serverMessager.getBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Transceiver.init();//初始化收发器
        serverMessager = new Messenger(new MessageServiceHandler());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
