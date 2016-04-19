package com.bili.diushoujuaner.application;

import android.app.Application;

import com.bili.diushoujuaner.R;
import com.bili.diushoujuaner.model.apihelper.HttpEngine;
import com.bili.diushoujuaner.model.apihelper.api.ApiAction;
import com.bili.diushoujuaner.model.cachehelper.ACache;
import com.bili.diushoujuaner.model.databasehelper.DBManager;
import com.bili.diushoujuaner.model.preferhelper.CustomSessionPreference;
import com.bili.diushoujuaner.model.preferhelper.SettingPreference;
import com.bili.diushoujuaner.presenter.messager.Transceiver;
import com.bili.diushoujuaner.utils.Common;
import com.bili.diushoujuaner.utils.MessageNoticer;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.stetho.Stetho;
import com.bili.diushoujuaner.utils.okhttp.okhttputils.OkHttpUtils;
import com.orhanobut.logger.Logger;
import com.squareup.leakcanary.LeakCanary;

import cat.ereza.customactivityoncrash.CustomActivityOnCrash;

/**
 * Created by BiLi on 2016/2/27.
 */
public class CustomApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initHtpp();// 初始化网络请求
        initFresco(); // 初始化Fresco
        initAcache();// 初始化ACache
        initPrefs(); // 初始化SharedPreference
        initDatabase();// 初始化DBManager
        MessageNoticer.init(this, R.raw.notify);
        Transceiver.init();
        if(Common.isApkDebugable(this)){
            initStetho();
            initLogger(); //初始化日志处理
            CustomActivityOnCrash.install(this);
            LeakCanary.install(this);
        }
    }

    private void initStetho(){
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(
                                Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(
                                Stetho.defaultInspectorModulesProvider(this))
                        .build());
    }

    private void initLogger(){
        if(Common.isApkDebugable(this)){
            Logger.init("guoyusen");
        }
    }

    private void initHtpp(){
        HttpEngine.initialize(this);
        ApiAction.initialize();
        OkHttpUtils.init(this);
        OkHttpUtils.getInstance()
                .setConnectTimeout(OkHttpUtils.DEFAULT_MILLISECONDS)               //全局的连接超时时间
                .setReadTimeOut(OkHttpUtils.DEFAULT_MILLISECONDS)                  //全局的读取超时时间
                .setWriteTimeOut(OkHttpUtils.DEFAULT_MILLISECONDS);
    }

    private void initAcache(){
        ACache.initialize(this);
    }

    private void initFresco() {
        Fresco.initialize(this);
    }

    private void initPrefs() {
        CustomSessionPreference.initialize(this);
        SettingPreference.initialize(this);
    }

    private void initDatabase(){
        DBManager.initialize(this);
    }
}
