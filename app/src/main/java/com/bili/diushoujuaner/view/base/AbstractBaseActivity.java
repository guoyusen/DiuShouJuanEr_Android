package com.bili.diushoujuaner.view.base;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.bili.diushoujuaner.R;
import com.bili.diushoujuaner.manager.ActivityManager;
import com.bili.diushoujuaner.manager.SystemBarTintManager;
import com.bili.diushoujuaner.view.application.CustomApplication;

/**
 * Created by BiLi on 2016/2/27.
 */
@TargetApi(Build.VERSION_CODES.KITKAT)
public abstract class AbstractBaseActivity extends Activity {

    public final String TAG = getClass().getSimpleName();
    public Context context;
    public CustomApplication customApplication;
    public SystemBarTintManager tintManager;

    /**
     * 初始化activity传递的参数
     */
    public abstract void initIntentParam(Intent intent);

    /**
     * 初始化页面之前的操作
     */
    public abstract void beforeInitView();

    /**
     * 定义页面控件
     */
    public abstract void initView();

    /**
     * 设置页面控件事件和状态
     */
    public abstract void setViewStatus();

    /**
     * == onResume()
     */
    public abstract void onPageResume();

    /**
     * == onPause()
     */
    public abstract void onPagePause();

    /**
     * == onDestroy()
     */
    public abstract void onPageDestroy();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        context = this;
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }
        tintStatusColor();
        ActivityManager.getInstance().addActivity(this);
        customApplication = (CustomApplication) this.getApplication();

        initIntentParam(getIntent());
        beforeInitView();
        initView();
        setViewStatus();
    }

    /**
     * 为状态栏着色
     */
    public void tintStatusColor() {
        tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.COLOR_THEME);
    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    @Override
    protected void onDestroy() {
        onPageDestroy();
        super.onDestroy();
        ActivityManager.getInstance().removeActivity(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        onPagePause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        onPageResume();
    }

    @Override
    public void setContentView(int layout) {
        ViewGroup mainView = (ViewGroup) LayoutInflater.from(this).inflate(
                layout, null);
        setContentView(mainView);
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
    }

    @Override
    public void finish() {
        super.finish();
    }
}
