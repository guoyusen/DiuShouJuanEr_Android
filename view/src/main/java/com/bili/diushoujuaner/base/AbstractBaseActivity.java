package com.bili.diushoujuaner.base;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.bili.diushoujuaner.R;
import com.bili.diushoujuaner.application.CustomApplication;
import com.bili.diushoujuaner.presenter.base.BasePresenter;
import com.bili.diushoujuaner.utils.manager.ActivityManager;
import com.bili.diushoujuaner.utils.manager.SystemBarTintManager;

import java.util.Date;

import butterknife.ButterKnife;

/**
 * Created by BiLi on 2016/2/27.
 */
@TargetApi(Build.VERSION_CODES.KITKAT)
public abstract class AbstractBaseActivity extends Activity {

    public final String TAG = getClass().getSimpleName();
    public Context context;
    public CustomApplication customApplication;
    public SystemBarTintManager tintManager;
    protected BasePresenter basePresenter;

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

    /**
     * == 显示加载框
     */
    public abstract void showLoading(int loadingType, String message);

    /**
     * == 隐藏加载框
     */
    public abstract void hideLoading(int loadingType);

    /**
     * == 显示警告信息
     */
    public abstract void showWarning(String message);

    public abstract void showPageHead(String titleText, Integer iconId, String rightText);

    public abstract void showPageHeadSpecial(String titleText, Integer iconId, Date rightText);

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
        ButterKnife.bind(this);
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
        ButterKnife.unbind(this);
        super.onDestroy();
        if(basePresenter != null){
            basePresenter.detachView();
        }
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
    public void finish() {
        super.finish();
    }

    protected <T> T getPresenterByClass(Class<T> t){
        return (T)basePresenter;
    }
}
