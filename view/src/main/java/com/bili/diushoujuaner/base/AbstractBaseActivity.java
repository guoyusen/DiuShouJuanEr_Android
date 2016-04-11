package com.bili.diushoujuaner.base;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.bili.diushoujuaner.R;
import com.bili.diushoujuaner.application.CustomApplication;
import com.bili.diushoujuaner.presenter.base.BasePresenter;
import com.bili.diushoujuaner.utils.manager.ActivityManager;
import com.bili.diushoujuaner.utils.manager.SystemBarTintManager;

import butterknife.ButterKnife;

/**
 * Created by BiLi on 2016/2/27.
 */
@TargetApi(Build.VERSION_CODES.KITKAT)
public abstract class AbstractBaseActivity extends Activity implements IBaseActivity{

    public final String TAG = getClass().getSimpleName();
    public Context context;
    public CustomApplication customApplication;
    public SystemBarTintManager tintManager;
    protected BasePresenter basePresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        ActivityManager.getInstance().addActivity(this);
        customApplication = (CustomApplication) this.getApplication();

        initIntentParam(getIntent());
        beforeInitView();
        initView();
        initHeader();
        ButterKnife.bind(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            tintStatusColor();
            resetStatus();
        }
        setViewStatus();
    }

    public void setTintStatusColor(int colorId){
        if(tintManager == null){
            return;
        }
        tintManager.setStatusBarTintResource(colorId);
    }

    public void setTineStatusAlpha(float alpha){
        if(tintManager == null){
            return;
        }
        tintManager.setStatusBarAlpha(alpha);
    }
    /**
     * 为状态栏着色
     */
    private void tintStatusColor() {
        tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.COLOR_THEME_MAIN);
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
        if(basePresenter != null){
            basePresenter.detachView();
        }
        onPageDestroy();
        ButterKnife.unbind(this);
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

}
