package com.bili.diushoujuaner.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Messenger;
import android.support.annotation.Nullable;

import com.bili.diushoujuaner.R;
import com.bili.diushoujuaner.activity.MainActivity;
import com.bili.diushoujuaner.model.messagehelper.MessageServiceHandler;
import com.bili.diushoujuaner.model.messagehelper.Transceiver;
import com.bili.diushoujuaner.utils.NotificationUtil;

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
        Transceiver.init(this);//初始化收发器
        NotificationUtil.init(this, MainActivity.class, R.mipmap.ic_launcher);
        serverMessager = new Messenger(new MessageServiceHandler(this));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
