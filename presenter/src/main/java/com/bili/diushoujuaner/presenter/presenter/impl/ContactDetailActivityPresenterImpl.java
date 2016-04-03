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
        RecallAction.getInstance().getRecentRecall(recentRecallReq, new ActionCallbackListener<ActionRespon<RecallDto>>() {
            @Override
            public void onSuccess(ActionRespon<RecallDto> result) {
                if(showMessage(result.getRetCode(), result.getMessage())){
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
    public void getFriendVo(long userNo) {
        showLoading(Constant.LOADING_DEFAULT,"");
        final FriendVo friendVo = ContactAction.getInstance().getContactFromLocal(userNo);
        if(friendVo == null || Common.isEmpty(friendVo.getUpdateTime()) || Common.getHourDifferenceBetweenTime(friendVo.getUpdateTime(),Common.getCurrentTimeYYMMDD_HHMMSS()) > 1){
            if(friendVo != null){
                getBindView().showContact(friendVo);
            }
            ContactInfoReq contactInfoReq = new ContactInfoReq();
            contactInfoReq.setUserNo(userNo);

            ContactAction.getInstance().getContactFromApi(contactInfoReq, new ActionCallbackListener<ActionRespon<FriendVo>>() {
                @Override
                public void onSuccess(ActionRespon<FriendVo> result) {
                    if(showMessage(result.getRetCode(), result.getMessage())){
                        getBindView().showContact(result.getData());
                    }else{
                        getBindView().showContact(friendVo);
                    }
                    getBindView().hideLoading(Constant.LOADING_DEFAULT);
                }

                @Override
                public void onFailure(int errorCode) {
                    showError(errorCode);
                    getBindView().showContact(friendVo);
                    getBindView().hideLoading(Constant.LOADING_DEFAULT);
                }
            });

        }else{
            getBindView().showContact(friendVo);
            getBindView().hideLoading(Constant.LOADING_DEFAULT);
        }
    }
}
