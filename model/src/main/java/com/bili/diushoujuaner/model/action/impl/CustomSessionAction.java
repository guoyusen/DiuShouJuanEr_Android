package com.bili.diushoujuaner.model.action.impl;


import android.content.Context;

import com.bili.diushoujuaner.model.action.ICustomSessionAction;
import com.bili.diushoujuaner.model.action.respon.ActionRespon;
import com.bili.diushoujuaner.model.apihelper.ApiRespon;
import com.bili.diushoujuaner.model.apihelper.api.ApiAction;
import com.bili.diushoujuaner.model.apihelper.callback.ApiCallbackListener;
import com.bili.diushoujuaner.model.preferhelper.CustomSessionPreference;
import com.bili.diushoujuaner.utils.GsonParser;
import com.bili.diushoujuaner.model.apihelper.response.CustomSession;
import com.bili.diushoujuaner.model.apihelper.request.UserAccountReq;
import com.bili.diushoujuaner.model.callback.ActionCallbackListener;
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
    public void getUserLogin(UserAccountReq userAccountReq, final ActionCallbackListener<ActionRespon<CustomSession>> actionCallbackListener){
        ApiAction.getInstance().getUserLogin(userAccountReq, new ApiCallbackListener() {
            @Override
            public void onSuccess(final String data) {
                Tasks.executeInBackground(context, new BackgroundWork<ActionRespon<CustomSession>>() {
                    @Override
                    public ActionRespon<CustomSession> doInBackground() throws Exception {
                        ApiRespon<CustomSession> result = GsonParser.getInstance().fromJson(data, new TypeToken<ApiRespon<CustomSession>>() {
                        }.getType());
                        CustomSessionPreference.getInstance().saveCustomSession(result.getData());
                        return ActionRespon.getActionResponFromApiRespon(result);
                    }
                }, new Completion<ActionRespon<CustomSession>>() {
                    @Override
                    public void onSuccess(Context context, ActionRespon<CustomSession> result) {
                        actionCallbackListener.onSuccess(result);
                    }

                    @Override
                    public void onError(Context context, Exception e) {
                        actionCallbackListener.onSuccess(ActionRespon.<CustomSession>getActionResponError());
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
