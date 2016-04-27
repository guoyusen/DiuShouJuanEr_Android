package com.bili.diushoujuaner.presenter.presenter.impl;

import android.content.Context;

import com.bili.diushoujuaner.model.actionhelper.action.ApplyAction;
import com.bili.diushoujuaner.model.actionhelper.action.ContactAction;
import com.bili.diushoujuaner.model.actionhelper.action.FeedBackAction;
import com.bili.diushoujuaner.model.actionhelper.action.UserInfoAction;
import com.bili.diushoujuaner.model.actionhelper.respon.ActionResponse;
import com.bili.diushoujuaner.model.apihelper.request.AutographModifyReq;
import com.bili.diushoujuaner.model.apihelper.request.FeedBackReq;
import com.bili.diushoujuaner.model.apihelper.request.FriendApplyReq;
import com.bili.diushoujuaner.model.apihelper.request.MemberNameUpdateReq;
import com.bili.diushoujuaner.model.apihelper.request.PartyApplyReq;
import com.bili.diushoujuaner.model.apihelper.request.PartyIntroduceUpdateReq;
import com.bili.diushoujuaner.model.apihelper.request.PartyNameUpdateReq;
import com.bili.diushoujuaner.model.apihelper.request.RemarkUpdateReq;
import com.bili.diushoujuaner.model.callback.ActionStringCallbackListener;
import com.bili.diushoujuaner.model.eventhelper.UpdateAutographEvent;
import com.bili.diushoujuaner.model.eventhelper.UpdatePartyEvent;
import com.bili.diushoujuaner.model.tempHelper.ChattingTemper;
import com.bili.diushoujuaner.presenter.base.BasePresenter;
import com.bili.diushoujuaner.presenter.base.IBaseView;
import com.bili.diushoujuaner.presenter.presenter.ContentEditActivityPresenter;
import com.bili.diushoujuaner.utils.ConstantUtil;
import com.bili.diushoujuaner.utils.StringUtil;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by BiLi on 2016/4/6.
 */
public class ContentEditActivityPresenterImpl extends BasePresenter<IBaseView> implements ContentEditActivityPresenter {

    public ContentEditActivityPresenterImpl(IBaseView baseView, Context context) {
        super(baseView, context);
    }

    @Override
    public void getFriendRemarkUpdate(long friendNo, String remark) {
        if(StringUtil.isEmpty(remark)){
            showWarning("备注名不能为空");
            return;
        }
        showLoading(ConstantUtil.LOADING_CENTER, "正在发送...");
        ContactAction.getInstance(context).getFriendRemarkUpdate(new RemarkUpdateReq(remark, friendNo), new ActionStringCallbackListener<ActionResponse<Void>>() {
            @Override
            public void onSuccess(ActionResponse<Void> result) {
                hideLoading(ConstantUtil.LOADING_CENTER);
                if(showMessage(result.getRetCode(), result.getMessage())){
                    showWarning("备注名修改成功");
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
    public void getPartyApply(long partyNo, String content) {
        if(StringUtil.isEmpty(content)){
            showWarning("验证信息不能为空");
            return;
        }
        showLoading(ConstantUtil.LOADING_CENTER, "正在发送...");
        ApplyAction.getInstance(context).getPartyApply(new PartyApplyReq(partyNo, content), new ActionStringCallbackListener<ActionResponse<Void>>() {
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
    public void publishNewPartyName(String partyName) {
        if(StringUtil.isEmpty(partyName)){
            showWarning("群名称不能为空");
            return;
        }
        long partyNo = 0;
        if(ChattingTemper.getInstance().getMsgType() == ConstantUtil.CHAT_PAR && isBindViewValid()){
            partyNo = ChattingTemper.getInstance().getToNo();
        }
        showLoading(ConstantUtil.LOADING_CENTER, "正在发送...");
        ContactAction.getInstance(context).getPartyNameUpdate(new PartyNameUpdateReq(partyNo, partyName), new ActionStringCallbackListener<ActionResponse<String>>() {
            @Override
            public void onSuccess(ActionResponse<String> result) {
                hideLoading(ConstantUtil.LOADING_CENTER);
                if(showMessage(result.getRetCode(), result.getMessage())){
                    showWarning("修改群名称成功");
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
    public void publishNewPartyIntroduce(String introduce) {
        long partyNo = 0;
        if(ChattingTemper.getInstance().getMsgType() == ConstantUtil.CHAT_PAR && isBindViewValid()){
            partyNo = ChattingTemper.getInstance().getToNo();
        }
        showLoading(ConstantUtil.LOADING_CENTER, "正在发送...");
        ContactAction.getInstance(context).getPartyIntroduceUpdate(new PartyIntroduceUpdateReq(partyNo, introduce), new ActionStringCallbackListener<ActionResponse<String>>() {
            @Override
            public void onSuccess(ActionResponse<String> result) {
                hideLoading(ConstantUtil.LOADING_CENTER);
                if(showMessage(result.getRetCode(), result.getMessage())){
                    showWarning("修改群介绍成功");
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
    public void publishNewMemberName(final String memberName) {
        if(StringUtil.isEmpty(memberName)){
            showWarning("群名片不能为空");
            return;
        }
        long partyNo = 0;
        if(ChattingTemper.getInstance().getMsgType() == ConstantUtil.CHAT_PAR && isBindViewValid()){
            partyNo = ChattingTemper.getInstance().getToNo();
        }
        showLoading(ConstantUtil.LOADING_CENTER, "正在发送...");
        ContactAction.getInstance(context).getMemberNameUpdate(new MemberNameUpdateReq(memberName, partyNo), new ActionStringCallbackListener<ActionResponse<String>>() {
            @Override
            public void onSuccess(ActionResponse<String> result) {
                hideLoading(ConstantUtil.LOADING_CENTER);
                if(showMessage(result.getRetCode(), result.getMessage())){
                    showWarning("修改群名片成功");
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
    public void publishNewFeedBack(String feedBack) {
        if(StringUtil.isEmpty(feedBack)){
            showWarning("意见反馈不能为空");
            return;
        }
        showLoading(ConstantUtil.LOADING_CENTER, "正在发送...");
        FeedBackAction.getInstance(context).getFeedBackAdd(new FeedBackReq(feedBack), new ActionStringCallbackListener<ActionResponse<Void>>() {
            @Override
            public void onSuccess(ActionResponse<Void> result) {
                hideLoading(ConstantUtil.LOADING_CENTER);
                if(showMessage(result.getRetCode(), result.getMessage())){
                    showWarning("意见反馈成功");
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
    public void publishNewAutograph(String autograph) {
        showLoading(ConstantUtil.LOADING_CENTER, "正在发送...");
        UserInfoAction.getInstance(context).getAutographModify(new AutographModifyReq(autograph), new ActionStringCallbackListener<ActionResponse<String>>() {
            @Override
            public void onSuccess(ActionResponse<String> result) {
                if(showMessage(result.getRetCode(), result.getMessage())){
                    showWarning("修改签名成功");
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

}
