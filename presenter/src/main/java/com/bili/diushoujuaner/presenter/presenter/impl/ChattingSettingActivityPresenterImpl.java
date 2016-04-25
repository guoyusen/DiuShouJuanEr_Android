package com.bili.diushoujuaner.presenter.presenter.impl;

import android.content.Context;

import com.bili.diushoujuaner.model.tempHelper.ChattingTemper;
import com.bili.diushoujuaner.model.tempHelper.ContactTemper;
import com.bili.diushoujuaner.presenter.base.BasePresenter;
import com.bili.diushoujuaner.presenter.presenter.ChattingSettingActivityPresenter;
import com.bili.diushoujuaner.presenter.view.IChattingSettingView;
import com.bili.diushoujuaner.utils.ConstantUtil;

/**
 * Created by BiLi on 2016/4/22.
 */
public class ChattingSettingActivityPresenterImpl extends BasePresenter<IChattingSettingView> implements ChattingSettingActivityPresenter {

    public ChattingSettingActivityPresenterImpl(IChattingSettingView baseView, Context context) {
        super(baseView, context);
    }

    @Override
    public void getContactInfo() {
        if(ChattingTemper.getInstance().getMsgType() == ConstantUtil.CHAT_FRI && isBindViewValid()){
            getBindView().showContactInfo(ContactTemper.getInstance().getFriendVo(ChattingTemper.getInstance().getToNo()));
        }
    }
}
