package com.bili.diushoujuaner.presenter.presenter.impl;

import android.content.Context;

import com.bili.diushoujuaner.model.actionhelper.action.ApplyAction;
import com.bili.diushoujuaner.model.actionhelper.respon.ActionRespon;
import com.bili.diushoujuaner.model.apihelper.request.FriendAgreeReq;
import com.bili.diushoujuaner.model.callback.ActionStringCallbackListener;
import com.bili.diushoujuaner.presenter.base.BasePresenter;
import com.bili.diushoujuaner.presenter.presenter.ContactAddActivityPresenter;
import com.bili.diushoujuaner.presenter.view.IContactAddView;
import com.bili.diushoujuaner.utils.Constant;
import com.bili.diushoujuaner.utils.entity.vo.ApplyVo;

import java.util.List;

/**
 * Created by BiLi on 2016/4/24.
 */
public class ContactAddActivityPresenterImpl extends BasePresenter<IContactAddView> implements ContactAddActivityPresenter {

    public ContactAddActivityPresenterImpl(IContactAddView baseView, Context context) {
        super(baseView, context);
    }

    @Override
    public void getFriendAgree(long fromNo, long toNo) {
        showLoading(Constant.LOADING_CENTER, "正在发送请求");
        ApplyAction.getInstance(context).getFriendAgree(new FriendAgreeReq(fromNo, toNo), new ActionStringCallbackListener<ActionRespon<Void>>() {
            @Override
            public void onSuccess(ActionRespon<Void> result) {
                hideLoading(Constant.LOADING_CENTER);
                if(showMessage(result.getRetCode(), result.getMessage())){
                    getApplyVoList();
                }
            }

            @Override
            public void onFailure(int errorCode) {
                showError(errorCode);
            }
        });
    }

    @Override
    public void getApplyVoList() {
        ApplyAction.getInstance(context).getApplyVoList(new ActionStringCallbackListener<ActionRespon<List<ApplyVo>>>() {
            @Override
            public void onSuccess(ActionRespon<List<ApplyVo>> result) {
                if(showMessage(result.getRetCode(), result.getMessage())){
                    if(isBindViewValid()){
                        getBindView().showApplyVoList(result.getData());
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
