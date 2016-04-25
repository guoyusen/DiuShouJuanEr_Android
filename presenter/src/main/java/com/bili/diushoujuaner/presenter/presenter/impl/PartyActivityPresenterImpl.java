package com.bili.diushoujuaner.presenter.presenter.impl;

import android.content.Context;

import com.bili.diushoujuaner.model.actionhelper.action.ContactAction;
import com.bili.diushoujuaner.model.tempHelper.ChattingTemper;
import com.bili.diushoujuaner.presenter.base.BasePresenter;
import com.bili.diushoujuaner.presenter.presenter.PartyActivityPresenter;
import com.bili.diushoujuaner.presenter.view.IPartyView;
import com.bili.diushoujuaner.utils.ConstantUtil;

/**
 * Created by BiLi on 2016/3/19.
 */
public class PartyActivityPresenterImpl extends BasePresenter<IPartyView> implements PartyActivityPresenter {

    public PartyActivityPresenterImpl(IPartyView baseView, Context context) {
        super(baseView, context);
    }

    @Override
    public void setCurrentChatting(long userNo, int msgType) {
        ChattingTemper.getInstance().setCurrentChatBo(userNo, msgType);
    }

    @Override
    public void getPartyList(){
        showLoading(ConstantUtil.LOADING_DEFAULT, "");
        if(isBindViewValid()){
            getBindView().showPartyList(ContactAction.getInstance(context).getPartyVoList());
        }
        hideLoading(ConstantUtil.LOADING_DEFAULT);
    }
}
