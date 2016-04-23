package com.bili.diushoujuaner.base;

import android.content.Intent;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bili.diushoujuaner.R;
import com.bili.diushoujuaner.activity.LoginActivity;
import com.bili.diushoujuaner.model.eventhelper.ForceOutEvent;
import com.bili.diushoujuaner.presenter.messager.LocalClient;
import com.bili.diushoujuaner.utils.Constant;
import com.bili.diushoujuaner.utils.manager.ActivityManager;
import com.bili.diushoujuaner.widget.CustomProgress;
import com.bili.diushoujuaner.widget.CustomToast;
import com.bili.diushoujuaner.widget.dialog.DialogTool;
import com.bili.diushoujuaner.widget.dialog.OnBothClickListener;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by BiLi on 2016/2/27.
 */
public class BaseActivity<T> extends AbstractBaseActivity {

    private TextView txtNavTitle;
    private TextView txtRight;
    private View defaultCircle;
    private ImageButton btnRight;

    protected T getBindPresenter(){
        return (T)basePresenter;
    }

    @Override
    public void initIntentParam(Intent intent) {

    }

    @Override
    public void beforeInitView() {

    }

    @Override
    public void initView() {

    }

    @Override
    public void setViewStatus() {

    }

    @Override
    public void onPageResume() {

    }

    @Override
    public void onPagePause() {

    }

    @Override
    public void onPageDestroy() {

    }

    @Override
    public void resetStatus() {

    }

    @Override
    public void initHeader() {
        defaultCircle = findViewById(R.id.defaultCircle);
        txtNavTitle = (TextView)findViewById(R.id.txtNavTitle);
        txtRight = (TextView)findViewById(R.id.txtRight);
        btnRight = (ImageButton)findViewById(R.id.btnRight);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return super.onKeyDown(keyCode, event);
        }
        return false;
    }

    public void back(View view) {
        finish();
    }

    @Override
    public void showPageHead(String titleText, Integer iconId, String rightText) {
        if(defaultCircle == null || txtNavTitle == null || txtRight == null || btnRight == null){
            return;
        }
        if(titleText == null){
            txtNavTitle.setVisibility(View.INVISIBLE);
        }else{
            txtNavTitle.setVisibility(View.VISIBLE);
            txtNavTitle.setText(titleText);
        }

        if(iconId == null){
            btnRight.setVisibility(View.GONE);
        }else{
            btnRight.setVisibility(View.VISIBLE);
            btnRight.setImageResource(iconId);
        }

        if(rightText == null){
            txtRight.setVisibility(View.INVISIBLE);
        }else{
            txtRight.setVisibility(View.VISIBLE);
            txtRight.setText(rightText);
        }
    }

    @Override
    public void showLoading(int loadingType, String message) {
        switch (loadingType){
            case Constant.LOADING_CENTER:
                CustomProgress.getInstance(context).showCenter(message, true, null);
                break;
            case Constant.LOADING_TOP:
                CustomProgress.getInstance(context).showTop(message, true, null);
                break;
            case Constant.LOADING_DEFAULT:
                if(defaultCircle != null){
                    defaultCircle.setVisibility(View.VISIBLE);
                    if(TextUtils.isEmpty(txtNavTitle.getText())){
                        txtNavTitle.setVisibility(View.GONE);
                    }
                }
                break;
        }
    }

    @Override
    public void hideLoading(int loadingType) {
        switch (loadingType){
            case Constant.LOADING_CENTER:
            case Constant.LOADING_TOP:
                CustomProgress.getInstance(context).dismiss();
                break;
            case Constant.LOADING_DEFAULT:
                if(findViewById(R.id.defaultCircle) != null){
                    findViewById(R.id.defaultCircle).setVisibility(View.GONE);
                }
                break;
        }
    }

    @Override
     public void showWarning(String message) {
        CustomToast.getInstance().showWarning(context, message);
    }

    @Override
    public void showWarning(int warningType) {
        switch (warningType){
            case Constant.WARNING_401:
                showWarning(context.getString(R.string.warning_401));
                break;
            case Constant.WARNING_403:
                showWarning(context.getString(R.string.warning_403));
                break;
            case Constant.WARNING_503:
                showWarning(context.getString(R.string.warning_503));
                break;
            case Constant.WARNING_500:
                showWarning(context.getString(R.string.warning_500));
                break;
            case Constant.WARNING_NET:
                showWarning(context.getString(R.string.warning_net));
                break;
            case Constant.WARNING_PARSE:
                showWarning(context.getString(R.string.warning_parse));
                break;
            case Constant.WARNING_FILE:
                showWarning(context.getString(R.string.warning_file));
                break;
        }
    }

    @Override
    public void exitActivity() {
        startActivity(new Intent(this, LoginActivity.class));
    }
}
