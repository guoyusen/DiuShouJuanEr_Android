package com.bili.diushoujuaner.presenter.presenter.impl;

import android.content.Context;

import com.bili.diushoujuaner.model.action.impl.UserInfoAction;
import com.bili.diushoujuaner.model.action.respon.ActionRespon;
import com.bili.diushoujuaner.model.apihelper.request.VerifyReq;
import com.bili.diushoujuaner.model.callback.ActionStringCallbackListener;
import com.bili.diushoujuaner.presenter.base.BasePresenter;
import com.bili.diushoujuaner.presenter.base.IBaseView;
import com.bili.diushoujuaner.presenter.presenter.AcountMobileFragmentPresenter;
import com.bili.diushoujuaner.utils.Constant;
import com.bili.diushoujuaner.utils.event.NextPageEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by BiLi on 2016/4/8.
 */
public class AcountMobileFragmentPresenterImpl extends BasePresenter<IBaseView> implements AcountMobileFragmentPresenter {

    public AcountMobileFragmentPresenterImpl(IBaseView baseView, Context context) {
        super(baseView, context);
    }

    @Override
    public void getVerifyCode(final String mobile, int type) {
        showLoading(Constant.LOADING_CENTER,"正在发送短信");
        VerifyReq verifyReq = new VerifyReq();
        verifyReq.setType(type);
        verifyReq.setMobile(mobile);
        UserInfoAction.getInstance(context).getVerifyCode(verifyReq, new ActionStringCallbackListener<ActionRespon<Void>>() {
            @Override
            public void onSuccess(ActionRespon<Void> result) {
                hideLoading(Constant.LOADING_CENTER);
                if(showMessage(result.getRetCode(), result.getMessage())){
                    EventBus.getDefault().post((new NextPageEvent(1)).setMobile(mobile));
                }
            }

            @Override
            public void onFailure(int errorCode) {
                showError(errorCode);
                hideLoading(Constant.LOADING_CENTER);
            }
        });

    }
}
