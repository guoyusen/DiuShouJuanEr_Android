package com.bili.diushoujuaner.presenter.presenter.impl;

import android.content.Context;

import com.bili.diushoujuaner.model.actionhelper.action.FileAction;
import com.bili.diushoujuaner.model.actionhelper.action.UserInfoAction;
import com.bili.diushoujuaner.model.actionhelper.respon.ActionResponse;
import com.bili.diushoujuaner.model.apihelper.request.UserInfoReq;
import com.bili.diushoujuaner.model.callback.ActionFileCallbackListener;
import com.bili.diushoujuaner.model.callback.ActionStringCallbackListener;
import com.bili.diushoujuaner.utils.ConstantUtil;
import com.bili.diushoujuaner.utils.entity.po.User;
import com.bili.diushoujuaner.presenter.base.BasePresenter;
import com.bili.diushoujuaner.presenter.presenter.UserActivityPresenter;
import com.bili.diushoujuaner.presenter.view.IUserView;
import com.bili.diushoujuaner.model.eventhelper.ShowHeadEvent;
import com.bili.diushoujuaner.model.eventhelper.UpdateUserInfoEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by BiLi on 2016/4/4.
 */
public class UserActivityPresenterImpl extends BasePresenter<IUserView> implements UserActivityPresenter {

    public UserActivityPresenterImpl(IUserView baseView, Context context) {
        super(baseView, context);
    }

    @Override
    public void updateHeadPic(String path) {
        showLoading(ConstantUtil.LOADING_TOP,"正在上传头像...");
        FileAction.getInstance(context).uploadHeadPic(path, new ActionFileCallbackListener<ActionResponse<String>>() {
            @Override
            public void onSuccess(ActionResponse<String> result) {
                hideLoading(ConstantUtil.LOADING_TOP);
                if(showMessage(result.getRetCode(), result.getMessage()) && isBindViewValid()){
                    getBindView().updateHeadPic(result.getData());
                }
            }

            @Override
            public void onFailure(int errorCode) {
                hideLoading(ConstantUtil.LOADING_TOP);
                showError(errorCode);
            }

            @Override
            public void onProgress(float progress) {

            }
        });
    }

    @Override
    public void updateUserInfo(UserInfoReq userInfoReq) {
        showLoading(ConstantUtil.LOADING_TOP, "正在保存资料...");
        UserInfoAction.getInstance(context).getUserInfoUpdate(userInfoReq, new ActionStringCallbackListener<ActionResponse<User>>() {
            @Override
            public void onSuccess(ActionResponse<User> result) {
                hideLoading(ConstantUtil.LOADING_TOP);
                if(showMessage(result.getRetCode(), result.getMessage()) && isBindViewValid()){
                    showWarning("资料保存成功");
                    getBindView().finishView();
                }
            }

            @Override
            public void onFailure(int errorCode) {
                hideLoading(ConstantUtil.LOADING_TOP);
                showError(errorCode);
            }
        });
    }

    @Override
    public void getUserInfo() {
        UserInfoAction.getInstance(context).getUserInfo(new ActionStringCallbackListener<ActionResponse<User>>() {
            @Override
            public void onSuccess(ActionResponse<User> result) {
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
