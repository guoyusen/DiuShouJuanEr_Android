package com.bili.diushoujuaner.model;


import android.content.Context;

import com.bili.diushoujuaner.model.api.ApiRespon;
import com.bili.diushoujuaner.model.api.api.ApiAction;
import com.bili.diushoujuaner.model.api.callback.ApiCallbackListener;
import com.bili.diushoujuaner.model.preference.AccessTokenPreference;
import com.bili.diushoujuaner.utils.Constant;
import com.bili.diushoujuaner.utils.response.AccessTokenDto;
import com.bili.diushoujuaner.utils.resquest.UserAccountDto;
import com.bili.diushoujuaner.model.callback.ActionCallbackListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nanotasks.BackgroundWork;
import com.nanotasks.Completion;
import com.nanotasks.Tasks;

/**
 * Created by BiLi on 2016/3/10.
 */
public class AccessTokenModel {

    private static AccessTokenModel accessTokenModel;
    private Context context;
    private Gson gson;

    private AccessTokenModel(Context context){
        this.context = context;
        gson = new Gson();
    }

    public static AccessTokenModel getInstance(Context context){
        if(accessTokenModel == null){
            accessTokenModel = new AccessTokenModel(context);
        }
        return accessTokenModel;
    }

    public void getAccessToken(final ActionCallbackListener<AccessTokenDto> actionCallbackListener){
        AccessTokenPreference accessTokenPreference = AccessTokenPreference.getInstance();
        AccessTokenDto accessTokenDto = accessTokenPreference.getAccessToken();
        if(accessTokenDto == null){
            actionCallbackListener.onSuccess(accessTokenDto);
        }else{
            actionCallbackListener.onFailure(Constant.CUSTOM_ERROR_CODE);
        }
    }

    public void getUserLogin(UserAccountDto userAccountDto, final ActionCallbackListener<ApiRespon<AccessTokenDto>> actionCallbackListener){
        ApiAction.getInstance().getUserLogin(userAccountDto, new ApiCallbackListener() {
            @Override
            public void onSuccess(final String data) {

                Tasks.executeInBackground(context, new BackgroundWork<ApiRespon<AccessTokenDto>>() {
                    @Override
                    public ApiRespon<AccessTokenDto> doInBackground() throws Exception {
                        return gson.fromJson(data, new TypeToken<ApiRespon<AccessTokenDto>>(){}.getType());
                    }
                }, new Completion<ApiRespon<AccessTokenDto>>() {
                    @Override
                    public void onSuccess(Context context, ApiRespon<AccessTokenDto> result) {
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
