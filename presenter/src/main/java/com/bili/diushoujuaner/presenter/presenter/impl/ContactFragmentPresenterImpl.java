package com.bili.diushoujuaner.presenter.presenter.impl;

import android.content.Context;

import com.bili.diushoujuaner.model.actionhelper.action.ApplyAction;
import com.bili.diushoujuaner.model.actionhelper.action.ContactAction;
import com.bili.diushoujuaner.model.actionhelper.respon.ActionResponse;
import com.bili.diushoujuaner.model.callback.ActionStringCallbackListener;
import com.bili.diushoujuaner.model.eventhelper.UpdateReadCountEvent;
import com.bili.diushoujuaner.model.tempHelper.ContactTemper;
import com.bili.diushoujuaner.presenter.base.BasePresenter;
import com.bili.diushoujuaner.presenter.presenter.ContactFragmentPresenter;
import com.bili.diushoujuaner.presenter.view.IContactView;
import com.bili.diushoujuaner.utils.ConstantUtil;
import com.bili.diushoujuaner.utils.entity.vo.FriendVo;
import com.bili.diushoujuaner.model.eventhelper.ContactUpdatedEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by BiLi on 2016/3/15.
 */
public class ContactFragmentPresenterImpl extends BasePresenter<IContactView> implements ContactFragmentPresenter {

    public ContactFragmentPresenterImpl(IContactView baseView, Context context) {
        super(baseView, context);
    }

    @Override
    public void updateApplyRead() {
        ApplyAction.getInstance(context).updateApplyRead(new ActionStringCallbackListener<ActionResponse<Void>>() {
            @Override
            public void onSuccess(ActionResponse<Void> result) {
                if(showMessage(result.getRetCode(), result.getMessage())){
                    EventBus.getDefault().post(new UpdateReadCountEvent(ConstantUtil.UNREAD_COUNT_APPLY, 0));
                }
            }

            @Override
            public void onFailure(int errorCode) {
                showError(errorCode);
            }
        });
    }

    @Override
    public void getAddUnReadCount() {
        ApplyAction.getInstance(context).getAddUnReadCount(new ActionStringCallbackListener<ActionResponse<Integer>>() {
            @Override
            public void onSuccess(ActionResponse<Integer> result) {
                if(showMessage(result.getRetCode(), result.getMessage())){
                    EventBus.getDefault().post(new UpdateReadCountEvent(ConstantUtil.UNREAD_COUNT_APPLY, result.getData()));
                }
            }
            @Override
            public void onFailure(int errorCode) {
                showError(errorCode);
            }
        });
    }

    public void getContactList(){
        showLoading(ConstantUtil.LOADING_DEFAULT, "");
        ContactTemper.getInstance().clear();
        ContactAction.getInstance(context).getContactList(true, new ActionStringCallbackListener<ActionResponse<List<FriendVo>>>() {
            @Override
            public void onSuccess(ActionResponse<List<FriendVo>> result) {
                hideLoading(ConstantUtil.LOADING_DEFAULT);
                if (showMessage(result.getRetCode(), result.getMessage())) {
                    if(isBindViewValid()){
                        getBindView().showContactList(result.getData());
                    }
                    EventBus.getDefault().post(new ContactUpdatedEvent());
                }
            }

            @Override
            public void onFailure(int errorCode) {
                hideLoading(ConstantUtil.LOADING_DEFAULT);
                showError(errorCode);
            }
        });
    }
}
