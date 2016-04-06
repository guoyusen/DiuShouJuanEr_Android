package com.bili.diushoujuaner.presenter.presenter.impl;

import android.content.Context;
import android.text.TextUtils;

import com.bili.diushoujuaner.model.action.respon.ActionRespon;
import com.bili.diushoujuaner.presenter.presenter.LoginActivityPresenter;
import com.bili.diushoujuaner.utils.Constant;
import com.bili.diushoujuaner.model.apihelper.response.CustomSession;
import com.bili.diushoujuaner.model.apihelper.request.UserAccountReq;
import com.bili.diushoujuaner.model.action.impl.CustomSessionAction;
import com.bili.diushoujuaner.model.callback.ActionCallbackListener;
import com.bili.diushoujuaner.presenter.base.BasePresenter;
import com.bili.diushoujuaner.presenter.view.ILoginView;
import com.bili.diushoujuaner.utils.Common;

/**
 * Created by BiLi on 2016/3/10.
 */
public class LoginActivityPresenterImpl extends BasePresenter<ILoginView> implements LoginActivityPresenter {

    public LoginActivityPresenterImpl(ILoginView baseView, Context context) {
        super(baseView, context);
    }

    @Override
    public void getUserLogin(String mobile, String psd){
        if(!validate(mobile, psd)){
            return;
        }
        showLoading(Constant.LOADING_TOP,"登录中...");

        UserAccountReq userAccountReq = new UserAccountReq();
        userAccountReq.setMobile(mobile);
        userAccountReq.setPassword(psd);
        CustomSessionAction.getInstance(context).getUserLogin(userAccountReq, new ActionCallbackListener<ActionRespon<CustomSession>>() {
            @Override
            public void onSuccess(ActionRespon<CustomSession> result) {
                if(showMessage(result.getRetCode(), result.getMessage())){
                    if(isBindViewValid()){
                        getBindView().loginSuccess();
                    }
                }
                hideLoading(Constant.LOADING_TOP);
            }

            @Override
            public void onFailure(int errorCode) {
                hideLoading(Constant.LOADING_TOP);
                showError(errorCode);
            }
        });
    }

    private boolean validate(String mobile, String psd) {
        if (TextUtils.isEmpty(mobile)) {
            showWarning("请输入手机号");
            return false;
        }
        if (!(Common.isMobile(mobile))) {
            showWarning("请输入正确的手机号码");
            return false;
        }
        if (TextUtils.isEmpty(psd)) {
            showWarning("请输入登录密码");
            return false;
        }
        return true;
    }

}
