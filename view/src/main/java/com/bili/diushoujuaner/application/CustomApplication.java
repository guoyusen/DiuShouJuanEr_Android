package com.bili.diushoujuaner.application;

import android.app.Application;

import com.bili.diushoujuaner.model.apihelper.HttpEngine;
import com.bili.diushoujuaner.model.apihelper.api.ApiAction;
import com.bili.diushoujuaner.model.cachehelper.ACache;
import com.bili.diushoujuaner.model.databasehelper.DBManager;
import com.bili.diushoujuaner.model.preferhelper.CustomSessionPreference;
import com.bili.diushoujuaner.utils.Common;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.stetho.Stetho;
import com.orhanobut.logger.Logger;

/**
 * Created by BiLi on 2016/2/27.
 */
public class CustomApplication extends Application {

    private static CustomApplication instance;

    public static CustomApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initHtpp();// 初始化网络请求
        initFresco(); // 初始化Fresco
        initAcache();// 初始化ACache
        initPrefs(); // 初始化SharedPreference
        initDatabase();// 初始化DBManager
        initLogger();
        Stetho.initializeWithDefaults(this);
    }

    private void initLogger(){
        if(Common.isApkDebugable(this)){
            Logger.init("guoyusen");
        }
    }

    private void initHtpp(){
        HttpEngine.initialize(this);
        ApiAction.initialize();
    }

    private void initAcache(){
        ACache.initialize(this);
    }

    private void initFresco() {
        Fresco.initialize(this);
    }

    private void initPrefs() {
        CustomSessionPreference.initialize(this);
    }

    private void initDatabase(){
        DBManager.initialize(this);
    }
}
