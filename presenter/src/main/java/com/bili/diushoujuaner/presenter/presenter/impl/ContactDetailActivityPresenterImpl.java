package com.bili.diushoujuaner.presenter.presenter.impl;

import android.content.Context;

import com.bili.diushoujuaner.model.actionhelper.action.ContactAction;
import com.bili.diushoujuaner.model.actionhelper.action.RecallAction;
import com.bili.diushoujuaner.model.actionhelper.respon.ActionRespon;
import com.bili.diushoujuaner.model.apihelper.request.ContactInfoReq;
import com.bili.diushoujuaner.model.apihelper.request.FriendDeleteReq;
import com.bili.diushoujuaner.model.apihelper.request.RecentRecallReq;
import com.bili.diushoujuaner.model.eventhelper.DeleteContactEvent;
import com.bili.diushoujuaner.model.preferhelper.CustomSessionPreference;
import com.bili.diushoujuaner.model.tempHelper.ChattingTemper;
import com.bili.diushoujuaner.model.tempHelper.ContactTemper;
import com.bili.diushoujuaner.utils.ConstantUtil;
import com.bili.diushoujuaner.utils.StringUtil;
import com.bili.diushoujuaner.utils.TimeUtil;
import com.bili.diushoujuaner.utils.entity.dto.RecallDto;
import com.bili.diushoujuaner.model.callback.ActionStringCallbackListener;
import com.bili.diushoujuaner.presenter.base.BasePresenter;
import com.bili.diushoujuaner.presenter.presenter.ContactDetailActivityPresenter;
import com.bili.diushoujuaner.presenter.view.IContactDetailView;
import com.bili.diushoujuaner.utils.entity.vo.FriendVo;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by BiLi on 2016/4/3.
 */
public class ContactDetailActivityPresenterImpl extends BasePresenter<IContactDetailView> implements ContactDetailActivityPresenter{

    public ContactDetailActivityPresenterImpl(IContactDetailView baseView, Context context) {
        super(baseView, context);
    }

    @Override
    public void getFriendDelete(final long friendNo) {
        showLoading(ConstantUtil.LOADING_CENTER, "正在删除");
        ContactAction.getInstance(context).getFriendDelete(new FriendDeleteReq(friendNo), new ActionStringCallbackListener<ActionRespon<Void>>() {
            @Override
            public void onSuccess(ActionRespon<Void> result) {
                hideLoading(ConstantUtil.LOADING_CENTER);
                if(showMessage(result.getRetCode(), result.getMessage())){
                    EventBus.getDefault().post(new DeleteContactEvent(ConstantUtil.DELETE_CONTACT_FRIEND, friendNo));
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
    public boolean isOwner(long userNo) {
        return CustomSessionPreference.getInstance().getCustomSession().getUserNo() == userNo;
    }

    @Override
    public boolean isFriend(long userNo) {
        return ContactTemper.getInstance().getFriendVo(userNo) != null;
    }

    @Override
    public void setCurrentChatting(long userNo, int msgType) {
        ChattingTemper.getInstance().setCurrentChatBo(userNo, msgType);
    }

    @Override
    public void getRecentRecall(long userNo) {
        RecentRecallReq recentRecallReq = new RecentRecallReq();
        recentRecallReq.setUserNo(userNo);
        RecallAction.getInstance(context).getRecentRecall(recentRecallReq, new ActionStringCallbackListener<ActionRespon<RecallDto>>() {
            @Override
            public void onSuccess(ActionRespon<RecallDto> result) {
                if (showMessage(result.getRetCode(), result.getMessage())) {
                    if (isBindViewValid()) {
                        getBindView().showRecent(result.getData());
                    }
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
        showLoading(ConstantUtil.LOADING_DEFAULT, "");
        ContactAction.getInstance(context).getContactFromLocal(userNo, new ActionStringCallbackListener<ActionRespon<FriendVo>>() {
            @Override
            public void onSuccess(ActionRespon<FriendVo> result) {
                //数据不合法，不去做界面处理，走api
                if (showMessage(result.getRetCode(), result.getMessage())) {
                    //TODO 更新获取联系人全量信息的时间间隔
                    if (result.getData() == null || StringUtil.isEmpty(result.getData().getUpdateTime()) || TimeUtil.getHourDifferenceBetweenTime(result.getData().getUpdateTime(), TimeUtil.getCurrentTimeYYMMDD_HHMMSS()) > 1) {
                        //数据已经无效，则在不为空的情况下，先进行显示，在进行更新获取
                        if (result.getData() != null && isBindViewValid()) {
                            getBindView().showContactInfo(result.getData());
                        }
                        getFriendVoFromApi(userNo);
                    }else{
                        //数据仍在有效期内，直接显示，无需更新
                        if(isBindViewValid()){
                            getBindView().showContactInfo(result.getData());
                        }
                        hideLoading(ConstantUtil.LOADING_DEFAULT);
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

        ContactAction.getInstance(context).getContactFromApi(contactInfoReq, new ActionStringCallbackListener<ActionRespon<FriendVo>>() {
            @Override
            public void onSuccess(ActionRespon<FriendVo> result) {
                if(showMessage(result.getRetCode(), result.getMessage())){
                    if(isBindViewValid()){
                        getBindView().showContactInfo(result.getData());
                    }
                }
                hideLoading(ConstantUtil.LOADING_DEFAULT);
            }

            @Override
            public void onFailure(int errorCode) {
                showError(errorCode);
                hideLoading(ConstantUtil.LOADING_DEFAULT);
            }
        });
    }
}
