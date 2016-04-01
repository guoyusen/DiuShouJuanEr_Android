package com.bili.diushoujuaner.model.action;


import com.bili.diushoujuaner.model.action.respon.ActionRespon;
import com.bili.diushoujuaner.model.apihelper.ApiRespon;
import com.bili.diushoujuaner.model.apihelper.api.ApiAction;
import com.bili.diushoujuaner.model.apihelper.callback.ApiCallbackListener;
import com.bili.diushoujuaner.model.preferhelper.CustomSessionPreference;
import com.bili.diushoujuaner.utils.Constant;
import com.bili.diushoujuaner.utils.GsonParser;
import com.bili.diushoujuaner.utils.response.CustomSession;
import com.bili.diushoujuaner.utils.request.UserAccountReq;
import com.bili.diushoujuaner.model.callback.ActionCallbackListener;
import com.google.gson.reflect.TypeToken;

/**
 * Created by BiLi on 2016/3/10.
 */
public class CustomSessionAction {

    private static CustomSessionAction customSessionAction;

    public static synchronized CustomSessionAction getInstance(){
        if(customSessionAction == null){
            customSessionAction = new CustomSessionAction();
        }
        return customSessionAction;
    }

    public void getIsLogined(ActionCallbackListener<ActionRespon<Boolean>> actionCallbackListener){
        actionCallbackListener.onSuccess(ActionRespon.getActionRespon(Constant.ACTION_LOAD_LOCAL_SUCCESS,Constant.RETCODE_SUCCESS,CustomSessionPreference.getInstance().isLogined()));
    }

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
