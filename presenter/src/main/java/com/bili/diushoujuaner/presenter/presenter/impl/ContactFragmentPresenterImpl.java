package com.bili.diushoujuaner.presenter.presenter.impl;

import android.content.Context;

import com.bili.diushoujuaner.model.actionhelper.action.ContactAction;
import com.bili.diushoujuaner.model.actionhelper.respon.ActionRespon;
import com.bili.diushoujuaner.model.callback.ActionStringCallbackListener;
import com.bili.diushoujuaner.presenter.base.BasePresenter;
import com.bili.diushoujuaner.presenter.presenter.ContactFragmentPresenter;
import com.bili.diushoujuaner.presenter.view.IContactView;
import com.bili.diushoujuaner.utils.Constant;
import com.bili.diushoujuaner.utils.entity.vo.FriendVo;
import com.bili.diushoujuaner.model.eventhelper.UpdatedContactEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by BiLi on 2016/3/15.
 */
public class ContactFragmentPresenterImpl extends BasePresenter<IContactView> implements ContactFragmentPresenter {

    public ContactFragmentPresenterImpl(IContactView baseView, Context context) {
        super(baseView, context);
    }

    public void getContactList(){
        showLoading(Constant.LOADING_DEFAULT, "");
        ContactAction.getInstance(context).getContactList(new ActionStringCallbackListener<ActionRespon<List<FriendVo>>>() {
            @Override
            public void onSuccess(ActionRespon<List<FriendVo>> result) {
                if (showMessage(result.getRetCode(), result.getMessage())) {
                    if(isBindViewValid()){
                        getBindView().showContactList(result.getData());
                    }
                    EventBus.getDefault().post(new UpdatedContactEvent());
                }
                hideLoading(Constant.LOADING_DEFAULT);
            }

            @Override
            public void onFailure(int errorCode) {
                showError(errorCode);
                hideLoading(Constant.LOADING_DEFAULT);
            }
        });
    }
}
