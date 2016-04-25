package com.bili.diushoujuaner.presenter.presenter.impl;

import android.content.Context;

import com.bili.diushoujuaner.model.actionhelper.action.UserInfoAction;
import com.bili.diushoujuaner.model.actionhelper.respon.ActionRespon;
import com.bili.diushoujuaner.model.apihelper.request.AcountUpdateReq;
import com.bili.diushoujuaner.model.apihelper.request.VerifyReq;
import com.bili.diushoujuaner.model.callback.ActionStringCallbackListener;
import com.bili.diushoujuaner.presenter.base.BasePresenter;
import com.bili.diushoujuaner.presenter.base.IBaseView;
import com.bili.diushoujuaner.presenter.presenter.AcountVerifyFragmentPresenter;
import com.bili.diushoujuaner.utils.ConstantUtil;
import com.bili.diushoujuaner.model.eventhelper.NextPageEvent;
import com.bili.diushoujuaner.model.eventhelper.StartTimerEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by BiLi on 2016/4/8.
 */
public class AcountVerifyFragmentPresenterImpl extends BasePresenter<IBaseView> implements AcountVerifyFragmentPresenter {

    public AcountVerifyFragmentPresenterImpl(IBaseView baseView, Context context) {
        super(baseView, context);
    }

    @Override
    public void getAcountRegister(String mobile, String password, String code) {
        showLoading(ConstantUtil.LOADING_CENTER, "正在注册账户");
        AcountUpdateReq acountUpdateReq = new AcountUpdateReq();
        acountUpdateReq.setMobile(mobile);
        acountUpdateReq.setCode(code);
        acountUpdateReq.setPassword(password);

        UserInfoAction.getInstance(context).getAcountRegist(acountUpdateReq, new ActionStringCallbackListener<ActionRespon<Void>>() {
            @Override
            public void onSuccess(ActionRespon<Void> result) {
                hideLoading(ConstantUtil.LOADING_CENTER);
                if(showMessage(result.getRetCode(), result.getMessage())){
                    EventBus.getDefault().post(new NextPageEvent(2));
                }
            }

            @Override
            public void onFailure(int errorCode) {
                hideLoading(ConstantUtil.LOADING_CENTER);
                showError(errorCode);
            }
        });
    }

    @Override
    public void getAcountReset(String mobile, String password, String code) {
        showLoading(ConstantUtil.LOADING_CENTER, "正在重置密码");
        AcountUpdateReq acountUpdateReq = new AcountUpdateReq();
        acountUpdateReq.setMobile(mobile);
        acountUpdateReq.setCode(code);
        acountUpdateReq.setPassword(password);

        UserInfoAction.getInstance(context).getAcountReset(acountUpdateReq, new ActionStringCallbackListener<ActionRespon<Void>>() {
            @Override
            public void onSuccess(ActionRespon<Void> result) {
                hideLoading(ConstantUtil.LOADING_CENTER);
                if(showMessage(result.getRetCode(), result.getMessage())){
                    EventBus.getDefault().post(new NextPageEvent(2));
                }
            }

            @Override
            public void onFailure(int errorCode) {
                hideLoading(ConstantUtil.LOADING_CENTER);
                showError(errorCode);
            }
        });
    }

    @Override
    public void getVerifyCode(final String mobile, int type) {
        showLoading(ConstantUtil.LOADING_CENTER,"正在发送短信");
        VerifyReq verifyReq = new VerifyReq();
        verifyReq.setType(type);
        verifyReq.setMobile(mobile);
        UserInfoAction.getInstance(context).getVerifyCode(verifyReq, new ActionStringCallbackListener<ActionRespon<Void>>() {
            @Override
            public void onSuccess(ActionRespon<Void> result) {
                hideLoading(ConstantUtil.LOADING_CENTER);
                if(showMessage(result.getRetCode(), result.getMessage())){
                    EventBus.getDefault().post((new StartTimerEvent()));
                }
            }

            @Override
            public void onFailure(int errorCode) {
                showError(errorCode);
                hideLoading(ConstantUtil.LOADING_CENTER);
            }
        });

    }
}
