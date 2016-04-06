package com.bili.diushoujuaner.presenter.presenter.impl;

import android.content.Context;

import com.bili.diushoujuaner.model.action.impl.ContactAction;
import com.bili.diushoujuaner.presenter.base.BasePresenter;
import com.bili.diushoujuaner.presenter.presenter.PartyActivityPresenter;
import com.bili.diushoujuaner.presenter.view.IPartyView;
import com.bili.diushoujuaner.utils.Constant;

/**
 * Created by BiLi on 2016/3/19.
 */
public class PartyActivityPresenterImpl extends BasePresenter<IPartyView> implements PartyActivityPresenter {

    public PartyActivityPresenterImpl(IPartyView baseView, Context context) {
        super(baseView, context);
    }

    @Override
    public void getPartyList(){
        showLoading(Constant.LOADING_DEFAULT, "");
        if(isBindViewValid()){
            getBindView().showPartyList(ContactAction.getInstance(context).getPartyVoList());
        }
        hideLoading(Constant.LOADING_DEFAULT);
    }
}
