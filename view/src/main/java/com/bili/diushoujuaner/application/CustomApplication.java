package com.bili.diushoujuaner.application;

import android.app.Application;

import com.bili.diushoujuaner.model.api.HttpEngine;
import com.bili.diushoujuaner.model.api.api.ApiAction;
import com.bili.diushoujuaner.model.cache.ACache;
import com.bili.diushoujuaner.model.preference.AccessTokenPreference;
import com.bili.diushoujuaner.model.preference.UserInfoPreference;
import com.facebook.drawee.backends.pipeline.Fresco;

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
        initHtpp();//初始化网络请求
        initAcache();//初始化缓存模块
        initImageLoader(); // 初始化图片加载器
        initPrefs(); // 初始化SharedPreference
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
        UserInfoPreference.initialize(this);
        AccessTokenPreference.initialize(this);
    }
}
