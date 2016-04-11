package com.bili.diushoujuaner.model.action.impl;


import android.content.Context;

import com.bili.diushoujuaner.model.action.ICustomSessionAction;
import com.bili.diushoujuaner.model.action.respon.ActionRespon;
import com.bili.diushoujuaner.model.apihelper.ApiRespon;
import com.bili.diushoujuaner.model.apihelper.api.ApiAction;
import com.bili.diushoujuaner.model.apihelper.callback.ApiStringCallbackListener;
import com.bili.diushoujuaner.model.preferhelper.CustomSessionPreference;
import com.bili.diushoujuaner.utils.GsonParser;
import com.bili.diushoujuaner.model.apihelper.response.CustomSession;
import com.bili.diushoujuaner.model.apihelper.request.UserAccountReq;
import com.bili.diushoujuaner.model.callback.ActionStringCallbackListener;
import com.google.gson.reflect.TypeToken;
import com.nanotasks.BackgroundWork;
import com.nanotasks.Completion;
import com.nanotasks.Tasks;

/**
 * Created by BiLi on 2016/3/10.
 */
public class CustomSessionAction implements ICustomSessionAction {

    private static CustomSessionAction customSessionAction;
    private Context context;

    public CustomSessionAction(Context context) {
        this.context = context;
    }

    public static synchronized CustomSessionAction getInstance(Context context){
        if(customSessionAction == null){
            customSessionAction = new CustomSessionAction(context);
        }
        return customSessionAction;
    }

    @Override
    public boolean getIsLogined(){
        return CustomSessionPreference.getInstance().isLogined();
    }

    @Override
    public void getUserLogin(UserAccountReq userAccountReq, final ActionStringCallbackListener<ActionRespon<Void>> actionStringCallbackListener){
        ApiAction.getInstance().getUserLogin(userAccountReq, new ApiStringCallbackListener() {
            @Override
            public void onSuccess(final String data) {
                Tasks.executeInBackground(context, new BackgroundWork<ActionRespon<Void>>() {
                @Override
                public ActionRespon<Void> doInBackground() throws Exception {
                    ApiRespon<CustomSession> result = GsonParser.getInstance().fromJson(data, new TypeToken<ApiRespon<CustomSession>>() {
                    }.getType());
                    if(result.getIsLegal()){
                        CustomSessionPreference.getInstance().saveCustomSession(result.getData());
                    }
                    return ActionRespon.getActionRespon(result.getMessage(), result.getRetCode(), (Void)null);
                }
            }, new Completion<ActionRespon<Void>>() {
                @Override
                public void onSuccess(Context context, ActionRespon<Void> result) {
                    actionStringCallbackListener.onSuccess(result);
                }

                @Override
                public void onError(Context context, Exception e) {
                    actionStringCallbackListener.onSuccess(ActionRespon.<Void>getActionResponError());
                }
            });
        }

            @Override
            public void onFailure(int errorCode) {
                actionStringCallbackListener.onFailure(errorCode);
            }
        });
    }

}
