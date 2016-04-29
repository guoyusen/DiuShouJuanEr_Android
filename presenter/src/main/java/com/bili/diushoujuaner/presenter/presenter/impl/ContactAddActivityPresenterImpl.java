package com.bili.diushoujuaner.presenter.presenter.impl;

import android.content.Context;

import com.bili.diushoujuaner.model.actionhelper.action.ApplyAction;
import com.bili.diushoujuaner.model.actionhelper.respon.ActionResponse;
import com.bili.diushoujuaner.model.apihelper.request.FriendAgreeReq;
import com.bili.diushoujuaner.model.apihelper.request.FriendApplyReq;
import com.bili.diushoujuaner.model.apihelper.request.PartyApplyAgreeReq;
import com.bili.diushoujuaner.model.callback.ActionStringCallbackListener;
import com.bili.diushoujuaner.presenter.base.BasePresenter;
import com.bili.diushoujuaner.presenter.presenter.ContactAddActivityPresenter;
import com.bili.diushoujuaner.presenter.view.IContactAddView;
import com.bili.diushoujuaner.utils.ConstantUtil;
import com.bili.diushoujuaner.utils.StringUtil;
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
    public void getFriendApply(long friendNo, String content) {
        if(StringUtil.isEmpty(content)){
            showWarning("验证信息不能为空");
            return;
        }
        showLoading(ConstantUtil.LOADING_CENTER, "正在发送...");
        ApplyAction.getInstance(context).getFriendApply(new FriendApplyReq(friendNo, content), new ActionStringCallbackListener<ActionResponse<Void>>() {
            @Override
            public void onSuccess(ActionResponse<Void> result) {
                hideLoading(ConstantUtil.LOADING_CENTER);
                if(showMessage(result.getRetCode(), result.getMessage())){
                    showWarning("请求成功，等待验证");
                    if(isBindViewValid()){
                        getBindView().finishView();
                    }
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
    public void getPartyApplyAgree(long partyNo, long memberNo) {
        showLoading(ConstantUtil.LOADING_CENTER, "正在发送请求...");
        ApplyAction.getInstance(context).getPartyApplyAgree(new PartyApplyAgreeReq(partyNo, memberNo), new ActionStringCallbackListener<ActionResponse<Void>>() {
            @Override
            public void onSuccess(ActionResponse<Void> result) {
                hideLoading(ConstantUtil.LOADING_CENTER);
                if(showMessage(result.getRetCode(), result.getMessage())){
                    getApplyVoList();
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
    public void getFriendApplyAgree(long friendNo) {
        showLoading(ConstantUtil.LOADING_CENTER, "正在发送请求...");
        ApplyAction.getInstance(context).getFriendApplyAgree(new FriendAgreeReq(friendNo), new ActionStringCallbackListener<ActionResponse<Void>>() {
            @Override
            public void onSuccess(ActionResponse<Void> result) {
                hideLoading(ConstantUtil.LOADING_CENTER);
                if(showMessage(result.getRetCode(), result.getMessage())){
                    getApplyVoList();
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
    public void getApplyVoList() {
        ApplyAction.getInstance(context).getApplyVoList(new ActionStringCallbackListener<ActionResponse<List<ApplyVo>>>() {
            @Override
            public void onSuccess(ActionResponse<List<ApplyVo>> result) {
                if(showMessage(result.getRetCode(), result.getMessage()) && isBindViewValid()){
                    getBindView().showApplyVoList(result.getData());
                }
            }

            @Override
            public void onFailure(int errorCode) {
                showError(errorCode);
            }
        });
    }
}
