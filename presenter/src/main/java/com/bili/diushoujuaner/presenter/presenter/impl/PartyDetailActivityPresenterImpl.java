package com.bili.diushoujuaner.presenter.presenter.impl;

import android.content.Context;

import com.bili.diushoujuaner.model.actionhelper.action.FileAction;
import com.bili.diushoujuaner.model.actionhelper.respon.ActionRespon;
import com.bili.diushoujuaner.model.apihelper.request.PartyHeadUpdateReq;
import com.bili.diushoujuaner.model.callback.ActionFileCallbackListener;
import com.bili.diushoujuaner.model.eventhelper.UpdatePartyEvent;
import com.bili.diushoujuaner.model.preferhelper.CustomSessionPreference;
import com.bili.diushoujuaner.model.tempHelper.ChattingTemper;
import com.bili.diushoujuaner.model.tempHelper.ContactTemper;
import com.bili.diushoujuaner.presenter.base.BasePresenter;
import com.bili.diushoujuaner.presenter.presenter.PartyDetailActivityPresenter;
import com.bili.diushoujuaner.presenter.view.IPartyDetailView;
import com.bili.diushoujuaner.utils.Constant;
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
    public void updatePartyHeadPic(final long partyNo, String path) {
        showLoading(Constant.LOADING_TOP,"正在上传头像");
        FileAction.getInstance(context).upoadPartyHeadPic(new PartyHeadUpdateReq(partyNo), path, new ActionFileCallbackListener<ActionRespon<String>>() {
            @Override
            public void onSuccess(ActionRespon<String> result) {
                hideLoading(Constant.LOADING_TOP);
                if(showMessage(result.getRetCode(), result.getMessage())){
                    if(isBindViewValid()){
                        EventBus.getDefault().post(new UpdatePartyEvent(partyNo, result.getData(), Constant.CHAT_PARTY_HEAD));
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
    public long getUserNo() {
        return CustomSessionPreference.getInstance().getCustomSession().getUserNo();
    }

    @Override
    public List<MemberVo> getMemberVoList() {
        long partyNo = 0;
        if(ChattingTemper.getInstance().getMsgType() == Constant.CHAT_PAR && isBindViewValid()){
            partyNo = ChattingTemper.getInstance().getToNo();
        }
        return ContactTemper.getInstance().getMemberVoList(partyNo);
    }

    @Override
    public String getMemberName() {
        long partyNo = 0;
        if(ChattingTemper.getInstance().getMsgType() == Constant.CHAT_PAR && isBindViewValid()){
            partyNo = ChattingTemper.getInstance().getToNo();
        }
        MemberVo memberVo = ContactTemper.getInstance().getMemberVo(partyNo, CustomSessionPreference.getInstance().getCustomSession().getUserNo());
        return memberVo != null ? memberVo.getMemberName() : "";
    }

    @Override
    public void getContactInfo() {
        if(ChattingTemper.getInstance().getMsgType() == Constant.CHAT_PAR && isBindViewValid()){
            getBindView().showContactInfo(ContactTemper.getInstance().getPartyVo(ChattingTemper.getInstance().getToNo()));
        }
    }
}
