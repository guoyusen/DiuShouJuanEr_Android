package com.bili.diushoujuaner.application;

import android.app.Application;

import com.bili.diushoujuaner.model.apihelper.HttpEngine;
import com.bili.diushoujuaner.model.apihelper.api.ApiAction;
import com.bili.diushoujuaner.model.cachehelper.ACache;
import com.bili.diushoujuaner.model.databasehelper.DBManager;
import com.bili.diushoujuaner.model.preferhelper.CustomSessionPreference;
import com.facebook.drawee.backends.pipeline.Fresco;
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
        initImageLoader(); // 初始化图片加载器
        initAcache();// 初始化缓存模块
        initPrefs(); // 初始化SharedPreference
        initDatabase();// 初始化DBManager
        initLogger();
    }

    private void initLogger(){
        Logger.init("guoyusen");
    }

    private void initHtpp(){
        HttpEngine.initialize(this);
        ApiAction.initialize(this);
    }

    private void initAcache(){
        ACache.initialize(this);
    }

    private void initImageLoader() {
        Fresco.initialize(this);
    }

    private void initPrefs() {
        CustomSessionPreference.initialize(this);
    }

    private void initDatabase(){
        DBManager.initialize(this);
    }
}
