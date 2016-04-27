package com.bili.diushoujuaner.presenter.presenter.impl;

import android.content.Context;

import com.bili.diushoujuaner.model.actionhelper.action.ContactAction;
import com.bili.diushoujuaner.model.actionhelper.respon.ActionResponse;
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
        ContactAction.getInstance(context).getContactsSearch(new ContactsSearchReq(paramNo), new ActionStringCallbackListener<ActionResponse<List<ContactDto>>>() {
            @Override
            public void onSuccess(ActionResponse<List<ContactDto>> result) {
                hideLoading(ConstantUtil.LOADING_CENTER);
                if(showMessage(result.getRetCode(), result.getMessage()) && isBindViewValid()){
                    getBindView().showSearchContactsResult(result.getData());
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
