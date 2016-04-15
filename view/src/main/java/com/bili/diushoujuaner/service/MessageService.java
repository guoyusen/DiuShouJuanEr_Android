package com.bili.diushoujuaner.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.bili.diushoujuaner.presenter.messager.MinaClienter;

/**
 * Created by BiLi on 2016/4/15.
 */
public class MessageService extends Service {

    public static final String TAG = "MessageService";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        MinaClienter.getInstance().start();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
