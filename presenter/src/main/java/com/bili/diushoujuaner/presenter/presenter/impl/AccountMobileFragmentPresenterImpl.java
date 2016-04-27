package com.bili.diushoujuaner.presenter.presenter.impl;

import android.content.Context;

import com.bili.diushoujuaner.model.actionhelper.action.UserInfoAction;
import com.bili.diushoujuaner.model.actionhelper.respon.ActionResponse;
import com.bili.diushoujuaner.model.apihelper.request.VerifyReq;
import com.bili.diushoujuaner.model.callback.ActionStringCallbackListener;
import com.bili.diushoujuaner.presenter.base.BasePresenter;
import com.bili.diushoujuaner.presenter.base.IBaseView;
import com.bili.diushoujuaner.presenter.presenter.AccountMobileFragmentPresenter;
import com.bili.diushoujuaner.utils.ConstantUtil;

/**
 * Created by BiLi on 2016/4/8.
 */
public class AccountMobileFragmentPresenterImpl extends BasePresenter<IBaseView> implements AccountMobileFragmentPresenter {

    public AccountMobileFragmentPresenterImpl(IBaseView baseView, Context context) {
        super(baseView, context);
    }

    @Override
    public void getVerifyCode(final String mobile, int type) {
        showLoading(ConstantUtil.LOADING_CENTER,"正在发送短信...");
        UserInfoAction.getInstance(context).getVerifyCode(new VerifyReq(mobile, type), new ActionStringCallbackListener<ActionResponse<Void>>() {
            @Override
            public void onSuccess(ActionResponse<Void> result) {
                hideLoading(ConstantUtil.LOADING_CENTER);
                showMessage(result.getRetCode(), result.getMessage());
            }

            @Override
            public void onFailure(int errorCode) {
                hideLoading(ConstantUtil.LOADING_CENTER);
                showError(errorCode);
            }
        });

    }
}
