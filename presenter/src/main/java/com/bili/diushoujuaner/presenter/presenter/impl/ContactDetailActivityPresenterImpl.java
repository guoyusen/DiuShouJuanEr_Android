package com.bili.diushoujuaner.presenter.presenter.impl;

import android.content.Context;

import com.bili.diushoujuaner.model.action.impl.ContactAction;
import com.bili.diushoujuaner.model.action.impl.RecallAction;
import com.bili.diushoujuaner.model.action.respon.ActionRespon;
import com.bili.diushoujuaner.model.apihelper.request.ContactInfoReq;
import com.bili.diushoujuaner.model.apihelper.request.RecentRecallReq;
import com.bili.diushoujuaner.model.apihelper.response.RecallDto;
import com.bili.diushoujuaner.model.callback.ActionCallbackListener;
import com.bili.diushoujuaner.presenter.base.BasePresenter;
import com.bili.diushoujuaner.presenter.presenter.ContactDetailActivityPresenter;
import com.bili.diushoujuaner.presenter.view.IContactDetailView;
import com.bili.diushoujuaner.utils.Common;
import com.bili.diushoujuaner.utils.Constant;
import com.bili.diushoujuaner.utils.entity.FriendVo;

/**
 * Created by BiLi on 2016/4/3.
 */
public class ContactDetailActivityPresenterImpl extends BasePresenter<IContactDetailView> implements ContactDetailActivityPresenter{

    public ContactDetailActivityPresenterImpl(IContactDetailView baseView, Context context) {
        super(baseView, context);
    }

    @Override
    public void getRecentRecall(long userNo) {
        RecentRecallReq recentRecallReq = new RecentRecallReq();
        recentRecallReq.setUserNo(userNo);
        RecallAction.getInstance(context).getRecentRecall(recentRecallReq, new ActionCallbackListener<ActionRespon<RecallDto>>() {
            @Override
            public void onSuccess(ActionRespon<RecallDto> result) {
                if (showMessage(result.getRetCode(), result.getMessage())) {
                    getBindView().showRecent(result.getData());
                }
            }

            @Override
            public void onFailure(int errorCode) {
                showError(errorCode);
            }
        });
    }

    @Override
    public void getFriendVo(final long userNo) {
        showLoading(Constant.LOADING_DEFAULT, "");
        ContactAction.getInstance(context).getContactFromLocal(userNo, new ActionCallbackListener<ActionRespon<FriendVo>>() {
            @Override
            public void onSuccess(ActionRespon<FriendVo> result) {
                //数据不合法，不去做界面处理，走api
                if (showMessage(result.getRetCode(), result.getMessage())) {
                    //TODO 更新获取联系人全量信息的时间间隔
                    if (result.getData() == null || Common.isEmpty(result.getData().getUpdateTime()) || Common.getHourDifferenceBetweenTime(result.getData().getUpdateTime(), Common.getCurrentTimeYYMMDD_HHMMSS()) > 1) {
                        //数据已经无效，则在不为空的情况下，先进行显示，在进行更新获取
                        if (result.getData() != null) {
                            getBindView().showContact(result.getData());
                        }
                        getFriendVoFromApi(userNo);
                    }else{
                        //数据仍在有效期内，直接显示，无需更新
                        getBindView().showContact(result.getData());
                        getBindView().hideLoading(Constant.LOADING_DEFAULT);
                    }
                }
            }

            @Override
            public void onFailure(int errorCode) {
            }
        });
    }

    private void getFriendVoFromApi(long userNo){
        ContactInfoReq contactInfoReq = new ContactInfoReq();
        contactInfoReq.setUserNo(userNo);

        ContactAction.getInstance(context).getContactFromApi(contactInfoReq, new ActionCallbackListener<ActionRespon<FriendVo>>() {
            @Override
            public void onSuccess(ActionRespon<FriendVo> result) {
                if(showMessage(result.getRetCode(), result.getMessage())){
                    getBindView().showContact(result.getData());
                }
                getBindView().hideLoading(Constant.LOADING_DEFAULT);
            }

            @Override
            public void onFailure(int errorCode) {
                //最不可能出现的情况，本地失败，API获取失败
                showError(errorCode);
                getBindView().showContact(null);
                getBindView().hideLoading(Constant.LOADING_DEFAULT);
            }
        });
    }
}
