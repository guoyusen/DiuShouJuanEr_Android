package com.bili.diushoujuaner.application;

import android.app.Application;

import com.bili.diushoujuaner.utils.ACache;
import com.bili.diushoujuaner.utils.UserInfoPreference;
import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by BiLi on 2016/2/27.
 */
public class CustomApplication extends Application {

    private static CustomApplication instance;
    private ACache aCache;
    private UserInfoPreference userInfoPreference;

    public static CustomApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initAcache();//初始化缓存模块
        initImageLoader(); // 初始化图片加载器
        initPrefs(); // 初始化SharedPreference
    }

    private void initAcache(){
        aCache = ACache.get(this);
    }

    private void initImageLoader() {
        Fresco.initialize(this);
    }

    private void initPrefs() {
        userInfoPreference = new UserInfoPreference(this);
    }
}
