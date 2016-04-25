package com.bili.diushoujuaner.presenter.presenter.impl;

import android.content.Context;

import com.bili.diushoujuaner.model.actionhelper.action.ContactAction;
import com.bili.diushoujuaner.model.actionhelper.respon.ActionRespon;
import com.bili.diushoujuaner.model.apihelper.request.ContactsSearchReq;
import com.bili.diushoujuaner.model.callback.ActionStringCallbackListener;
import com.bili.diushoujuaner.presenter.base.BasePresenter;
import com.bili.diushoujuaner.presenter.presenter.ContactSearchActivityPresenter;
import com.bili.diushoujuaner.presenter.view.IContactSearchView;
import com.bili.diushoujuaner.utils.ConstantUtil;
import com.bili.diushoujuaner.utils.entity.dto.ContactDto;

import java.util.List;

/**
 * Created by BiLi on 2016/4/23.
 */
public class ContactSearchActivityPresenterImpl extends BasePresenter<IContactSearchView> implements ContactSearchActivityPresenter {

    public ContactSearchActivityPresenterImpl(IContactSearchView baseView, Context context) {
        super(baseView, context);
    }

    @Override
    public void getContactsSearch(String paramNo) {
        showLoading(ConstantUtil.LOADING_CENTER, "正在搜索...");
        ContactsSearchReq contactsSearchReq = new ContactsSearchReq(paramNo);
        ContactAction.getInstance(context).getContactsSearch(contactsSearchReq, new ActionStringCallbackListener<ActionRespon<List<ContactDto>>>() {
            @Override
            public void onSuccess(ActionRespon<List<ContactDto>> result) {
                hideLoading(ConstantUtil.LOADING_CENTER);
                if(showMessage(result.getRetCode(), result.getMessage())){
                    if(isBindViewValid() && result.getData() != null){
                        getBindView().showSearchContactsResult(result.getData());
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
