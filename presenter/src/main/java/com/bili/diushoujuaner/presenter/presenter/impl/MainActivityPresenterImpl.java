package com.bili.diushoujuaner.presenter.presenter.impl;

import android.content.Context;

import com.bili.diushoujuaner.model.actionhelper.action.UserInfoAction;
import com.bili.diushoujuaner.model.actionhelper.respon.ActionRespon;
import com.bili.diushoujuaner.model.callback.ActionStringCallbackListener;
import com.bili.diushoujuaner.model.tempHelper.ChattingTemper;
import com.bili.diushoujuaner.model.tempHelper.ContactTemper;
import com.bili.diushoujuaner.model.tempHelper.GoodTemper;
import com.bili.diushoujuaner.model.tempHelper.RecallTemper;
import com.bili.diushoujuaner.utils.entity.po.User;
import com.bili.diushoujuaner.presenter.base.BasePresenter;
import com.bili.diushoujuaner.presenter.presenter.MainActivityPresenter;
import com.bili.diushoujuaner.presenter.view.IMainView;

/**
 * Created by BiLi on 2016/3/13.
 */
public class MainActivityPresenterImpl extends BasePresenter<IMainView> implements MainActivityPresenter {

    public MainActivityPresenterImpl(IMainView baseView, Context context) {
        super(baseView, context);
    }

    @Override
    public void clear() {
        UserInfoAction.getInstance(context).clearUser();
        RecallTemper.getInstance().clear();
        GoodTemper.getInstance().clear();
        ContactTemper.getInstance().clear();
        ChattingTemper.getInstance().clear(true);
    }

    @Override
    public void getUserInfo(){
        UserInfoAction.getInstance(context).getUserInfo(new ActionStringCallbackListener<ActionRespon<User>>() {
            @Override
            public void onSuccess(ActionRespon<User> result) {
                if(showMessage(result.getRetCode(), result.getMessage())){
                    if(result.getData()!= null && isBindViewValid()){
                        getBindView().showUserInfo(result.getData());
                    }
                }
            }

            @Override
            public void onFailure(int errorCode) {
                showError(errorCode);
            }
        });
    }
}
