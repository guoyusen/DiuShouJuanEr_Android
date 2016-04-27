package com.bili.diushoujuaner.presenter.presenter.impl;

import android.content.Context;

import com.bili.diushoujuaner.model.actionhelper.action.ContactAction;
import com.bili.diushoujuaner.model.actionhelper.respon.ActionResponse;
import com.bili.diushoujuaner.model.apihelper.request.PartyAddReq;
import com.bili.diushoujuaner.model.callback.ActionFileCallbackListener;
import com.bili.diushoujuaner.presenter.base.BasePresenter;
import com.bili.diushoujuaner.presenter.base.IBaseView;
import com.bili.diushoujuaner.presenter.presenter.BuildPartyActivityPresenter;
import com.bili.diushoujuaner.utils.ConstantUtil;
import com.bili.diushoujuaner.utils.StringUtil;

/**
 * Created by BiLi on 2016/4/25.
 */
public class BuildPartyActivityPresenterImpl extends BasePresenter<IBaseView> implements BuildPartyActivityPresenter {

    public BuildPartyActivityPresenterImpl(IBaseView baseView, Context context) {
        super(baseView, context);
    }

    @Override
    public void getPartyAdd(String partyName, String path) {
        if(partyName.length() < 2){
            showWarning("群名称不能少于2字符");
            return;
        }
        if(StringUtil.isEmpty(path)){
            showWarning("请选择头像");
            return;
        }
        showLoading(ConstantUtil.LOADING_CENTER, "正在创建群...");
        ContactAction.getInstance(context).getPartyAdd(new PartyAddReq(partyName), path, new ActionFileCallbackListener<ActionResponse<Void>>() {
            @Override
            public void onSuccess(ActionResponse<Void> result) {
                hideLoading(ConstantUtil.LOADING_CENTER);
                if(showMessage(result.getRetCode(), result.getMessage()) && isBindViewValid()){
                    getBindView().finishView();
                }
            }

            @Override
            public void onFailure(int errorCode) {
                hideLoading(ConstantUtil.LOADING_CENTER);
                showError(errorCode);
            }

            @Override
            public void onProgress(float progress) {

            }
        });
    }
}
