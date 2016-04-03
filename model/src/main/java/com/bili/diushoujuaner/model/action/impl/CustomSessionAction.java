package com.bili.diushoujuaner.model.action.impl;


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

/**
 * Created by BiLi on 2016/3/10.
 */
public class CustomSessionAction implements ICustomSessionAction {

    private static CustomSessionAction customSessionAction;

    public static synchronized CustomSessionAction getInstance(){
        if(customSessionAction == null){
            customSessionAction = new CustomSessionAction();
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
                ApiRespon<CustomSession> result = GsonParser.getInstance().fromJson(data, new TypeToken<ApiRespon<CustomSession>>() {
                }.getType());
                CustomSessionPreference.getInstance().saveCustomSession(result.getData());
                actionCallbackListener.onSuccess(ActionRespon.getActionRespon(result.getMessage(),result.getRetCode(),result.getData()));
            }

            @Override
            public void onFailure(int errorCode) {
                actionCallbackListener.onFailure(errorCode);
            }
        });
    }

}
