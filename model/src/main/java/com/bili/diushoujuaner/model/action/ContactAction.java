package com.bili.diushoujuaner.model.action;

import android.content.Context;

import com.bili.diushoujuaner.model.apihelper.ApiRespon;
import com.bili.diushoujuaner.model.apihelper.api.ApiAction;
import com.bili.diushoujuaner.model.apihelper.callback.ApiCallbackListener;
import com.bili.diushoujuaner.model.callback.ActionCallbackListener;
import com.bili.diushoujuaner.utils.GsonParser;
import com.bili.diushoujuaner.utils.response.ContactDto;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * Created by BiLi on 2016/3/15.
 */
public class ContactAction {

    private static ContactAction contactAction;

    public static synchronized ContactAction getInstance(){
        if(contactAction == null){
            contactAction = new ContactAction();
        }
        return contactAction;
    }

    public void getContactList(final ActionCallbackListener<ApiRespon<List<ContactDto>>> actionCallbackListener){
        ApiAction.getInstance().getContactList(new ApiCallbackListener() {
            @Override
            public void onSuccess(final String data) {
                ApiRespon<List<ContactDto>> result = GsonParser.getInstance().fromJson(data, new TypeToken<ApiRespon<List<ContactDto>>>() {
                }.getType());
                actionCallbackListener.onSuccess(result);
            }

            @Override
            public void onFailure(int errorCode) {
                actionCallbackListener.onFailure(errorCode);
            }
        });
    }

}
