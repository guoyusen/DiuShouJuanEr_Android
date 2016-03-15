package com.bili.diushoujuaner.presenter.presenter;

import android.content.Context;

import com.bili.diushoujuaner.model.action.ContactsAction;
import com.bili.diushoujuaner.model.apihelper.ApiRespon;
import com.bili.diushoujuaner.model.callback.ActionCallbackListener;
import com.bili.diushoujuaner.model.databasehelper.dao.Friend;
import com.bili.diushoujuaner.presenter.base.BasePresenter;
import com.bili.diushoujuaner.presenter.base.IBaseView;
import com.bili.diushoujuaner.presenter.viewinterface.ContactFragmentView;
import com.bili.diushoujuaner.utils.Common;
import com.bili.diushoujuaner.utils.Constant;
import com.bili.diushoujuaner.utils.response.ContactDto;

import java.util.List;

/**
 * Created by BiLi on 2016/3/15.
 */
public class ContactFragmentPresenter extends BasePresenter {

    public ContactFragmentPresenter(IBaseView baseView, Context context) {
        super(baseView, context);
    }

    public void getContacts(){
        getViewByClass(ContactFragmentView.class).showLoading(Constant.LOADING_DEFAULT);
        if(Common.checkNetworkStatus(context)){
            ContactsAction.getInstance(context).getContacts(new ActionCallbackListener<ApiRespon<List<ContactDto>>>() {
                @Override
                public void onSuccess(ApiRespon<List<ContactDto>> result) {
                    if(showMessage(result.getRetCode(), result.getMessage())){
                        saveContactFromList(result.getData());
                    }
                    getViewByClass(ContactFragmentView.class).hideLoading(Constant.LOADING_DEFAULT);
                }

                @Override
                public void onFailure(int errorCode) {
                    showError(errorCode);
                    getViewByClass(ContactFragmentView.class).hideLoading(Constant.LOADING_DEFAULT);
                }
            });
        }else{

            getViewByClass(ContactFragmentView.class).hideLoading(Constant.LOADING_DEFAULT);
        }
    }

    private Friend getFriendFromDb(){
        return null;
    }

    private void saveContactFromList(List<ContactDto> contactDtoList){

    }

}
