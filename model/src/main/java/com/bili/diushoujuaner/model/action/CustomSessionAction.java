package com.bili.diushoujuaner.model.action;


import android.content.Context;

import com.bili.diushoujuaner.model.apihelper.ApiRespon;
import com.bili.diushoujuaner.model.apihelper.api.ApiAction;
import com.bili.diushoujuaner.model.apihelper.callback.ApiCallbackListener;
import com.bili.diushoujuaner.utils.GsonParser;
import com.bili.diushoujuaner.utils.response.CustomSession;
import com.bili.diushoujuaner.utils.request.UserAccountReq;
import com.bili.diushoujuaner.model.callback.ActionCallbackListener;
import com.google.gson.reflect.TypeToken;
import com.nanotasks.BackgroundWork;
import com.nanotasks.Completion;
import com.nanotasks.Tasks;

/**
 * Created by BiLi on 2016/3/10.
 */
public class CustomSessionAction {

    private static CustomSessionAction customSessionAction;
    private Context context;

    private CustomSessionAction(Context context){
        this.context = context;
    }

    public static CustomSessionAction getInstance(Context context){
        if(customSessionAction == null){
            customSessionAction = new CustomSessionAction(context);
        }
        return customSessionAction;
    }

    public void getUserLogin(UserAccountReq userAccountReq, final ActionCallbackListener<ApiRespon<CustomSession>> actionCallbackListener){
        ApiAction.getInstance().getUserLogin(userAccountReq, new ApiCallbackListener() {
            @Override
            public void onSuccess(final String data) {

                Tasks.executeInBackground(context, new BackgroundWork<ApiRespon<CustomSession>>() {
                    @Override
                    public ApiRespon<CustomSession> doInBackground() throws Exception {
                        return GsonParser.getInstance().fromJson(data, new TypeToken<ApiRespon<CustomSession>>() {
                        }.getType());
                    }
                }, new Completion<ApiRespon<CustomSession>>() {
                    @Override
                    public void onSuccess(Context context, ApiRespon<CustomSession> result) {
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
