package com.bili.diushoujuaner.presenter.presenter.impl;

import android.content.Context;

import com.bili.diushoujuaner.model.action.impl.UserInfoAction;
import com.bili.diushoujuaner.model.action.respon.ActionRespon;
import com.bili.diushoujuaner.model.callback.ActionCallbackListener;
import com.bili.diushoujuaner.model.databasehelper.dao.User;
import com.bili.diushoujuaner.presenter.base.BasePresenter;
import com.bili.diushoujuaner.presenter.presenter.ContentEditActivityPresenter;
import com.bili.diushoujuaner.presenter.view.IContentEditView;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by BiLi on 2016/4/6.
 */
public class ContentEditActivityPresenterImpl extends BasePresenter<IContentEditView> implements ContentEditActivityPresenter {

    public ContentEditActivityPresenterImpl(IContentEditView baseView, Context context) {
        super(baseView, context);
    }

    @Override
    public void putNewAutograph(String autograph) {

    }

    @Override
    public void getOldAutograph() {
        UserInfoAction.getInstance(context).getUserInfo(new ActionCallbackListener<ActionRespon<User>>() {
            @Override
            public void onSuccess(ActionRespon<User> result) {
                if(showMessage(result.getRetCode(), result.getMessage())){
                    if(result.getData()!= null && isBindViewValid()){
                        getBindView().showHint(result.getData().getAutograph());
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
