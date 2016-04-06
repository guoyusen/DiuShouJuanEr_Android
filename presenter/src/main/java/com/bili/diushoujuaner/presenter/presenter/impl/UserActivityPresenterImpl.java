package com.bili.diushoujuaner.presenter.presenter.impl;

import android.content.Context;

import com.bili.diushoujuaner.model.action.impl.UserInfoAction;
import com.bili.diushoujuaner.model.action.respon.ActionRespon;
import com.bili.diushoujuaner.model.callback.ActionCallbackListener;
import com.bili.diushoujuaner.model.databasehelper.dao.User;
import com.bili.diushoujuaner.presenter.base.BasePresenter;
import com.bili.diushoujuaner.presenter.presenter.UserActivityPresenter;
import com.bili.diushoujuaner.presenter.view.IUserView;

/**
 * Created by BiLi on 2016/4/4.
 */
public class UserActivityPresenterImpl extends BasePresenter<IUserView> implements UserActivityPresenter {

    public UserActivityPresenterImpl(IUserView baseView, Context context) {
        super(baseView, context);
    }

    @Override
    public void getUserInfo() {
        UserInfoAction.getInstance(context).getUserInfo(new ActionCallbackListener<ActionRespon<User>>() {
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
