package com.bili.diushoujuaner.presenter.presenter.impl;

import android.content.Context;

import com.bili.diushoujuaner.model.actionhelper.action.ContactAction;
import com.bili.diushoujuaner.model.actionhelper.respon.ActionResponse;
import com.bili.diushoujuaner.model.apihelper.request.MemberForceExitReq;
import com.bili.diushoujuaner.model.callback.ActionStringCallbackListener;
import com.bili.diushoujuaner.presenter.base.BasePresenter;
import com.bili.diushoujuaner.presenter.presenter.MemberActivityPresenter;
import com.bili.diushoujuaner.presenter.view.IMemberView;
import com.bili.diushoujuaner.utils.ConstantUtil;
import com.bili.diushoujuaner.utils.entity.vo.MemberVo;

import java.util.List;

/**
 * Created by BiLi on 2016/4/26.
 */
public class MemberActivityPresenterImpl extends BasePresenter<IMemberView> implements MemberActivityPresenter {

    public MemberActivityPresenterImpl(IMemberView baseView, Context context) {
        super(baseView, context);
    }

    @Override
    public void getMemberForceExit(long partyNo, long memberNo) {
        showLoading(ConstantUtil.LOADING_CENTER, "正在踢除...");
        ContactAction.getInstance(context).getMemberForceExit(new MemberForceExitReq(partyNo, memberNo), new ActionStringCallbackListener<ActionResponse<List<MemberVo>>>() {
            @Override
            public void onSuccess(ActionResponse<List<MemberVo>> result) {
                hideLoading(ConstantUtil.LOADING_CENTER);
                if(showMessage(result.getRetCode(), result.getMessage())){
                    getBindView().showMemberList(result.getData());
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
    public void getMemberList(long partyNo) {
        showLoading(ConstantUtil.LOADING_DEFAULT, "");
        ContactAction.getInstance(context).getMemberVoListFromLocal(partyNo, new ActionStringCallbackListener<ActionResponse<List<MemberVo>>>() {
            @Override
            public void onSuccess(ActionResponse<List<MemberVo>> result) {
                hideLoading(ConstantUtil.LOADING_DEFAULT);
                if(showMessage(result.getRetCode(), result.getMessage()) && isBindViewValid()){
                    getBindView().showMemberList(result.getData());
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
