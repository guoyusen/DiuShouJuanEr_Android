package com.bili.diushoujuaner.presenter.presenter;

import android.text.TextUtils;

import com.bili.diushoujuaner.presenter.base.BasePresenter;
import com.bili.diushoujuaner.presenter.viewinterface.LoginActivityView;
import com.bili.diushoujuaner.utils.Common;

/**
 * Created by BiLi on 2016/3/10.
 */
public class LoginActivityPresenter extends BasePresenter {

    public LoginActivityPresenter(LoginActivityView loginActivityView){
        attachView(loginActivityView);
    }

    public void getUserLogin(String mobile, String psd){
        if(!validate(mobile, psd)){
            return;
        }
        getViewByClass(LoginActivityView.class).showLoading();

    }

    private boolean validate(String mobile, String psd) {
        if (TextUtils.isEmpty(mobile)) {
            getViewByClass(LoginActivityView.class).LoginFail("请输入手机号");
            return false;
        }
        if (!(Common.isMobile(mobile))) {
            getViewByClass(LoginActivityView.class).LoginFail("请输入正确的手机号码");
            return false;
        }
        if (TextUtils.isEmpty(psd)) {
            getViewByClass(LoginActivityView.class).LoginFail("请输入登录密码");
            return false;
        }
        return true;
    }

}
