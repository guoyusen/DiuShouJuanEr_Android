package com.bili.diushoujuaner.presenter.presenter.impl;

import android.content.Context;
import android.util.Log;

import com.bili.diushoujuaner.model.action.impl.FileAction;
import com.bili.diushoujuaner.model.action.impl.UserInfoAction;
import com.bili.diushoujuaner.model.action.respon.ActionRespon;
import com.bili.diushoujuaner.model.apihelper.request.UserInfoReq;
import com.bili.diushoujuaner.model.callback.ActionFileCallbackListener;
import com.bili.diushoujuaner.model.callback.ActionStringCallbackListener;
import com.bili.diushoujuaner.model.databasehelper.dao.User;
import com.bili.diushoujuaner.model.okhttphelper.okhttpserver.listener.UploadListener;
import com.bili.diushoujuaner.model.okhttphelper.okhttpserver.upload.UploadInfo;
import com.bili.diushoujuaner.presenter.base.BasePresenter;
import com.bili.diushoujuaner.presenter.presenter.UserActivityPresenter;
import com.bili.diushoujuaner.presenter.view.IUserView;
import com.bili.diushoujuaner.utils.Constant;
import com.bili.diushoujuaner.utils.event.ShowHeadEvent;
import com.bili.diushoujuaner.utils.event.UpdateUserInfoEvent;

import org.greenrobot.eventbus.EventBus;

import okhttp3.Response;

/**
 * Created by BiLi on 2016/4/4.
 */
public class UserActivityPresenterImpl extends BasePresenter<IUserView> implements UserActivityPresenter {

    public UserActivityPresenterImpl(IUserView baseView, Context context) {
        super(baseView, context);
    }

    @Override
    public void updateHeadPic(String path) {
        showLoading(Constant.LOADING_TOP,"正在上传头像");
        FileAction.getInstance(context).uploadHeadPic(path, new ActionFileCallbackListener<ActionRespon<String>>() {
            @Override
            public void onSuccess(ActionRespon<String> result) {
                hideLoading(Constant.LOADING_TOP);
                if(showMessage(result.getRetCode(), result.getMessage())){
                    if(isBindViewValid()){
                        EventBus.getDefault().post(new ShowHeadEvent(result.getData()));
                        getBindView().updateHeadPic(result.getData());
                    }
                }
            }

            @Override
            public void onFailure(int errorCode) {
                hideLoading(Constant.LOADING_TOP);
                showError(errorCode);
            }

            @Override
            public void onProgress(float progress) {

            }
        });
    }

    @Override
    public void updateUserInfo(UserInfoReq userInfoReq) {
        showLoading(Constant.LOADING_TOP, "正在保存资料");
        UserInfoAction.getInstance(context).getUserInfoUpdate(userInfoReq, new ActionStringCallbackListener<ActionRespon<User>>() {
            @Override
            public void onSuccess(ActionRespon<User> result) {
                if(showMessage(result.getRetCode(), result.getMessage())){
                    EventBus.getDefault().post(new UpdateUserInfoEvent());
                }
                showWarning("资料保存成功");
                hideLoading(Constant.LOADING_TOP);
                if(isBindViewValid()){
                    getBindView().finishView();
                }
            }

            @Override
            public void onFailure(int errorCode) {
                showError(errorCode);
                hideLoading(Constant.LOADING_TOP);
            }
        });
    }

    @Override
    public void getUserInfo() {
        UserInfoAction.getInstance(context).getUserInfo(new ActionStringCallbackListener<ActionRespon<User>>() {
            @Override
            public void onSuccess(ActionRespon<User> result) {
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
