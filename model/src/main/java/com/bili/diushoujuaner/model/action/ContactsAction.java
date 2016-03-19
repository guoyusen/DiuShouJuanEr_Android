package com.bili.diushoujuaner.model.action;

import android.content.Context;

import com.bili.diushoujuaner.model.apihelper.ApiRespon;
import com.bili.diushoujuaner.model.apihelper.api.ApiAction;
import com.bili.diushoujuaner.model.apihelper.callback.ApiCallbackListener;
import com.bili.diushoujuaner.model.callback.ActionCallbackListener;
import com.bili.diushoujuaner.utils.GsonParser;
import com.bili.diushoujuaner.utils.entity.PartyVo;
import com.bili.diushoujuaner.utils.response.ContactDto;
import com.google.gson.reflect.TypeToken;
import com.nanotasks.BackgroundWork;
import com.nanotasks.Completion;
import com.nanotasks.Tasks;

import java.util.List;

/**
 * Created by BiLi on 2016/3/15.
 */
public class ContactsAction {

    private static ContactsAction contactsAction;
    private Context context;

    private ContactsAction(Context context){
        this.context = context;
    }

    public static ContactsAction getInstance(Context context){
        if(contactsAction == null){
            contactsAction = new ContactsAction(context);
        }
        return contactsAction;
    }

    public void getContactList(final ActionCallbackListener<ApiRespon<List<ContactDto>>> actionCallbackListener){
        ApiAction.getInstance().getContactList(new ApiCallbackListener() {
            @Override
            public void onSuccess(final String data) {
                Tasks.executeInBackground(context, new BackgroundWork<ApiRespon<List<ContactDto>>>() {
                    @Override
                    public ApiRespon<List<ContactDto>> doInBackground() throws Exception {
                        return GsonParser.getInstance().fromJson(data, new TypeToken<ApiRespon<List<ContactDto>>>() {
                        }.getType());
                    }
                }, new Completion<ApiRespon<List<ContactDto>>>() {
                    @Override
                    public void onSuccess(Context context, ApiRespon<List<ContactDto>> result) {
                        actionCallbackListener.onSuccess(result);
                    }

                    @Override
                    public void onError(Context context, Exception e) {
                    }
                });
            }

            @Override
            public void onFailure(int errorCode) {
                actionCallbackListener.onFailure(errorCode);
            }
        });
    }

}
