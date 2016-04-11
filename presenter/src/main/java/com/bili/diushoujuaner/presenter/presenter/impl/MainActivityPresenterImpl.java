package com.bili.diushoujuaner.presenter.presenter.impl;

import android.content.Context;

import com.bili.diushoujuaner.model.action.impl.UserInfoAction;
import com.bili.diushoujuaner.model.action.respon.ActionRespon;
import com.bili.diushoujuaner.model.callback.ActionStringCallbackListener;
import com.bili.diushoujuaner.model.databasehelper.dao.User;
import com.bili.diushoujuaner.presenter.base.BasePresenter;
import com.bili.diushoujuaner.presenter.presenter.MainActivityPresenter;
import com.bili.diushoujuaner.presenter.view.IMainView;
import com.bili.diushoujuaner.utils.event.ShowHeadEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by BiLi on 2016/3/13.
 */
public class MainActivityPresenterImpl extends BasePresenter<IMainView> implements MainActivityPresenter {

    public MainActivityPresenterImpl(IMainView baseView, Context context) {
        super(baseView, context);
    }

    @Override
    public void getUserInfo(){
        UserInfoAction.getInstance(context).getUserInfo(new ActionStringCallbackListener<ActionRespon<User>>() {
            @Override
            public void onSuccess(ActionRespon<User> result) {
                if(showMessage(result.getRetCode(), result.getMessage())){
                    if(result.getData()!= null && isBindViewValid()){
                        EventBus.getDefault().post(new ShowHeadEvent(result.getData().getPicPath()));
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
