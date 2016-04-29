package com.bili.diushoujuaner.presenter.presenter.impl;

import android.content.Context;

import com.bili.diushoujuaner.model.actionhelper.action.ContactAction;
import com.bili.diushoujuaner.model.actionhelper.respon.ActionResponse;
import com.bili.diushoujuaner.model.apihelper.request.BatchMemberAddReq;
import com.bili.diushoujuaner.model.callback.ActionStringCallbackListener;
import com.bili.diushoujuaner.presenter.base.BasePresenter;
import com.bili.diushoujuaner.presenter.presenter.MemberAddActivityPresenter;
import com.bili.diushoujuaner.presenter.view.IMemberAddView;
import com.bili.diushoujuaner.utils.ConstantUtil;
import com.bili.diushoujuaner.utils.GsonUtil;
import com.bili.diushoujuaner.utils.entity.vo.FriendVo;
import com.bili.diushoujuaner.utils.entity.vo.MemberVo;

import java.util.List;

/**
 * Created by BiLi on 2016/4/26.
 */
public class MemberAddActivityPresenterImpl extends BasePresenter<IMemberAddView> implements MemberAddActivityPresenter {

    public MemberAddActivityPresenterImpl(IMemberAddView baseView, Context context) {
        super(baseView, context);
    }

    @Override
    public void getMemberVoList(long partyNo) {
        ContactAction.getInstance(context).getMemberVoListFromLocal(partyNo, new ActionStringCallbackListener<ActionResponse<List<MemberVo>>>() {
            @Override
            public void onSuccess(ActionResponse<List<MemberVo>> result) {
                if(showMessage(result.getRetCode(), result.getMessage()) && isBindViewValid()){
                    getBindView().showMemberVoList(result.getData());
                }
            }

            @Override
            public void onFailure(int errorCode) {
                showError(errorCode);
            }
        });
    }

    @Override
    public void getMembersAddToParty(long partyNo, List<Long> members) {
        showLoading(ConstantUtil.LOADING_CENTER, "正在添加");
        ContactAction.getInstance(context).getMembersAddToParty(new BatchMemberAddReq(partyNo, GsonUtil.getInstance().toJson(members)), new ActionStringCallbackListener<ActionResponse<Void>>() {
            @Override
            public void onSuccess(ActionResponse<Void> result) {
                hideLoading(ConstantUtil.LOADING_CENTER);
                if(showMessage(result.getRetCode(), result.getMessage())){
                    showWarning("邀请已成功发送，等待确认");
                    getBindView().finishView();
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
    public void getContactList() {
        showLoading(ConstantUtil.LOADING_DEFAULT, "");
        ContactAction.getInstance(context).getContactList(false, new ActionStringCallbackListener<ActionResponse<List<FriendVo>>>() {
            @Override
            public void onSuccess(ActionResponse<List<FriendVo>> result) {
                hideLoading(ConstantUtil.LOADING_DEFAULT);
                if (showMessage(result.getRetCode(), result.getMessage()) && isBindViewValid()) {
                    getBindView().showContactList(result.getData());
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
