package com.bili.diushoujuaner.presenter.presenter.impl;

import android.content.Context;

import com.bili.diushoujuaner.model.actionhelper.action.ContactAction;
import com.bili.diushoujuaner.model.actionhelper.action.FileAction;
import com.bili.diushoujuaner.model.actionhelper.respon.ActionRespon;
import com.bili.diushoujuaner.model.apihelper.request.MemberExitReq;
import com.bili.diushoujuaner.model.apihelper.request.PartyHeadUpdateReq;
import com.bili.diushoujuaner.model.callback.ActionFileCallbackListener;
import com.bili.diushoujuaner.model.callback.ActionStringCallbackListener;
import com.bili.diushoujuaner.model.eventhelper.UpdatePartyEvent;
import com.bili.diushoujuaner.model.preferhelper.CustomSessionPreference;
import com.bili.diushoujuaner.model.tempHelper.ChattingTemper;
import com.bili.diushoujuaner.model.tempHelper.ContactTemper;
import com.bili.diushoujuaner.presenter.base.BasePresenter;
import com.bili.diushoujuaner.presenter.presenter.PartyDetailActivityPresenter;
import com.bili.diushoujuaner.presenter.view.IPartyDetailView;
import com.bili.diushoujuaner.utils.ConstantUtil;
import com.bili.diushoujuaner.utils.entity.vo.MemberVo;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by BiLi on 2016/4/22.
 */
public class PartyDetailActivityPresenterImpl extends BasePresenter<IPartyDetailView> implements PartyDetailActivityPresenter {

    public PartyDetailActivityPresenterImpl(IPartyDetailView baseView, Context context) {
        super(baseView, context);
    }

    @Override
    public void getMemberExit() {
        getMemberExit(ChattingTemper.getInstance().getToNo());
    }

    @Override
    public void getMemberExit(long partyNo) {
        showLoading(ConstantUtil.LOADING_CENTER, "正在退出...");
        ContactAction.getInstance(context).getMemberExit(new MemberExitReq(partyNo), new ActionStringCallbackListener<ActionRespon<Void>>() {
            @Override
            public void onSuccess(ActionRespon<Void> result) {
                ChattingTemper.getInstance().resetCurrentChatBo();
                hideLoading(ConstantUtil.LOADING_CENTER);
                showMessage(result.getRetCode(), result.getMessage());
            }

            @Override
            public void onFailure(int errorCode) {
                hideLoading(ConstantUtil.LOADING_CENTER);
                showError(errorCode);
            }
        });
    }

    @Override
    public boolean isPartied(long partyNo) {
        return ContactTemper.getInstance().getPartyVo(partyNo) != null;
    }

    @Override
    public void updatePartyHeadPic(final long partyNo, String path) {
        showLoading(ConstantUtil.LOADING_TOP,"正在上传头像");
        FileAction.getInstance(context).upoadPartyHeadPic(new PartyHeadUpdateReq(partyNo), path, new ActionFileCallbackListener<ActionRespon<String>>() {
            @Override
            public void onSuccess(ActionRespon<String> result) {
                hideLoading(ConstantUtil.LOADING_TOP);
                if(showMessage(result.getRetCode(), result.getMessage())){
                    if(isBindViewValid()){
                        EventBus.getDefault().post(new UpdatePartyEvent(partyNo, result.getData(), ConstantUtil.CHAT_PARTY_HEAD));
                    }
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
    public long getUserNo() {
        return CustomSessionPreference.getInstance().getCustomSession().getUserNo();
    }

    @Override
    public List<MemberVo> getMemberVoList(long partyNo) {
        return ContactTemper.getInstance().getMemberVoList(partyNo);
    }

    @Override
    public List<MemberVo> getMemberVoList() {
        long partyNo = 0;
        if(ChattingTemper.getInstance().getMsgType() == ConstantUtil.CHAT_PAR && isBindViewValid()){
            partyNo = ChattingTemper.getInstance().getToNo();
        }
        return ContactTemper.getInstance().getMemberVoList(partyNo);
    }

    @Override
    public String getMemberName() {
        long partyNo = 0;
        if(ChattingTemper.getInstance().getMsgType() == ConstantUtil.CHAT_PAR && isBindViewValid()){
            partyNo = ChattingTemper.getInstance().getToNo();
        }
        MemberVo memberVo = ContactTemper.getInstance().getMemberVo(partyNo, CustomSessionPreference.getInstance().getCustomSession().getUserNo());
        return memberVo != null ? memberVo.getMemberName() : "";
    }

    @Override
    public void getContactInfo(long partyNo) {
        getBindView().showContactInfo(ContactTemper.getInstance().getPartyVo(partyNo));
    }

    @Override
    public void getContactInfo() {
        getBindView().showContactInfo(ContactTemper.getInstance().getPartyVo(ChattingTemper.getInstance().getToNo()));
    }
}
