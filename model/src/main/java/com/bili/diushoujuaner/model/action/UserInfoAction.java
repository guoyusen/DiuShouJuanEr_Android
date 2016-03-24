package com.bili.diushoujuaner.model.action;

import android.content.Context;

import com.bili.diushoujuaner.model.apihelper.ApiRespon;
import com.bili.diushoujuaner.model.apihelper.api.ApiAction;
import com.bili.diushoujuaner.model.apihelper.callback.ApiCallbackListener;
import com.bili.diushoujuaner.model.callback.ActionCallbackListener;
import com.bili.diushoujuaner.utils.GsonParser;
import com.bili.diushoujuaner.utils.response.CustomSession;
import com.bili.diushoujuaner.utils.response.UserRes;
import com.google.gson.reflect.TypeToken;

/**
 * Created by BiLi on 2016/3/13.
 */
public class UserInfoAction {

    private static UserInfoAction userInfoAction;

    public static synchronized UserInfoAction getInstance(){
        if(userInfoAction == null){
            userInfoAction = new UserInfoAction();
        }
        return userInfoAction;
    }

    public void getUserInfo(final ActionCallbackListener<ApiRespon<UserRes>> actionCallbackListener){
        ApiAction.getInstance().getUserInfo(new ApiCallbackListener() {
            @Override
            public void onSuccess(final String data) {
                ApiRespon<UserRes> result = GsonParser.getInstance().fromJson(data, new TypeToken<ApiRespon<UserRes>>() {
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
