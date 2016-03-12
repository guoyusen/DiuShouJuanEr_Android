package com.bili.diushoujuaner.presenter.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.bili.diushoujuaner.model.apihelper.ApiRespon;
import com.bili.diushoujuaner.model.preferhelper.CustomSessionPreference;
import com.bili.diushoujuaner.presenter.base.IBaseView;
import com.bili.diushoujuaner.utils.Constant;
import com.bili.diushoujuaner.utils.response.CustomSession;
import com.bili.diushoujuaner.utils.resquest.UserAccountDto;
import com.bili.diushoujuaner.model.action.CustomSessionAction;
import com.bili.diushoujuaner.model.callback.ActionCallbackListener;
import com.bili.diushoujuaner.presenter.base.BasePresenter;
import com.bili.diushoujuaner.presenter.viewinterface.LoginActivityView;
import com.bili.diushoujuaner.utils.Common;

/**
 * Created by BiLi on 2016/3/10.
 */
public class LoginActivityPresenter extends BasePresenter {

    public LoginActivityPresenter(IBaseView baseView, Context context) {
        super(baseView, context);
    }

    public void getUserLogin(String mobile, String psd){
        if(!validate(mobile, psd)){
            return;
        }
        getViewByClass(LoginActivityView.class).showLoading(Constant.LOADING_TOP);

        UserAccountDto userAccountDto = new UserAccountDto();
        userAccountDto.setMobile(mobile);
        userAccountDto.setPassword(psd);
        CustomSessionAction.getInstance(context).getUserLogin(userAccountDto, new ActionCallbackListener<ApiRespon<CustomSession>>() {
            @Override
            public void onSuccess(ApiRespon<CustomSession> result) {
                if(showMessage(result.getRetCode(), result.getMessage())){
                    CustomSessionPreference.getInstance().saveAccessToken(result.getData());
                    getViewByClass(LoginActivityView.class).loginSuccess();
                }
                getViewByClass(LoginActivityView.class).hideLoading(Constant.LOADING_TOP);
            }

            @Override
            public void onFailure(int errorCode) {
                getViewByClass(LoginActivityView.class).hideLoading(Constant.LOADING_TOP);
                showError(errorCode);
            }
        });
    }

    private boolean validate(String mobile, String psd) {
        if (TextUtils.isEmpty(mobile)) {
            getViewByClass(LoginActivityView.class).showWarning("请输入手机号");
            return false;
        }
        if (!(Common.isMobile(mobile))) {
            getViewByClass(LoginActivityView.class).showWarning("请输入正确的手机号码");
            return false;
        }
        if (TextUtils.isEmpty(psd)) {
            getViewByClass(LoginActivityView.class).showWarning("请输入登录密码");
            return false;
        }
        return true;
    }

}
