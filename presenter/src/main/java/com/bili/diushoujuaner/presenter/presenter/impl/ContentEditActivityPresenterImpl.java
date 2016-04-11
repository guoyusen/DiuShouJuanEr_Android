package com.bili.diushoujuaner.presenter.presenter.impl;

import android.content.Context;

import com.bili.diushoujuaner.model.action.impl.FeedBackAction;
import com.bili.diushoujuaner.model.action.impl.UserInfoAction;
import com.bili.diushoujuaner.model.action.respon.ActionRespon;
import com.bili.diushoujuaner.model.apihelper.request.AutographModifyReq;
import com.bili.diushoujuaner.model.apihelper.request.FeedBackReq;
import com.bili.diushoujuaner.model.callback.ActionStringCallbackListener;
import com.bili.diushoujuaner.model.databasehelper.dao.User;
import com.bili.diushoujuaner.presenter.base.BasePresenter;
import com.bili.diushoujuaner.presenter.presenter.ContentEditActivityPresenter;
import com.bili.diushoujuaner.presenter.view.IContentEditView;
import com.bili.diushoujuaner.utils.event.UpdateAutographEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by BiLi on 2016/4/6.
 */
public class ContentEditActivityPresenterImpl extends BasePresenter<IContentEditView> implements ContentEditActivityPresenter {

    public ContentEditActivityPresenterImpl(IContentEditView baseView, Context context) {
        super(baseView, context);
    }

    @Override
    public void publishNewFeedBack(String feedBack) {
        FeedBackReq feedBackReq = new FeedBackReq();
        feedBackReq.setContent(feedBack);

        FeedBackAction.getInstance(context).getFeedBackAdd(feedBackReq, new ActionStringCallbackListener<ActionRespon<Void>>() {
            @Override
            public void onSuccess(ActionRespon<Void> result) {
                if(showMessage(result.getRetCode(), result.getMessage())){
                    if(isBindViewValid()){
                        getBindView().finishView();
                    }
                    showWarning("意见反馈成功");
                }
            }

            @Override
            public void onFailure(int errorCode) {
                showError(errorCode);
            }
        });
    }

    @Override
    public void publishNewAutograph(String autograph) {
        AutographModifyReq autographModifyReq = new AutographModifyReq();
        autographModifyReq.setAutograph(autograph);
        UserInfoAction.getInstance(context).getAutographModify(autographModifyReq, new ActionStringCallbackListener<ActionRespon<String>>() {
            @Override
            public void onSuccess(ActionRespon<String> result) {
                if(showMessage(result.getRetCode(), result.getMessage())){
                    if(isBindViewValid()){
                        getBindView().finishView();
                    }
                    showWarning("修改签名成功");
                    EventBus.getDefault().post(new UpdateAutographEvent(result.getData()));
                }
            }

            @Override
            public void onFailure(int errorCode) {
                showError(errorCode);
            }
        });
    }

    @Override
    public void getOldAutograph() {
        UserInfoAction.getInstance(context).getUserInfo(new ActionStringCallbackListener<ActionRespon<User>>() {
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
