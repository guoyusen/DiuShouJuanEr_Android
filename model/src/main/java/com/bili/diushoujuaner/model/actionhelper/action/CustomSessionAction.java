package com.bili.diushoujuaner.model.actionhelper.action;


import android.content.Context;

import com.bili.diushoujuaner.model.actionhelper.ICustomSessionAction;
import com.bili.diushoujuaner.model.actionhelper.respon.ActionResponse;
import com.bili.diushoujuaner.model.apihelper.ApiResponse;
import com.bili.diushoujuaner.model.apihelper.api.ApiAction;
import com.bili.diushoujuaner.model.apihelper.callback.ApiStringCallbackListener;
import com.bili.diushoujuaner.model.preferhelper.CustomSessionPreference;
import com.bili.diushoujuaner.utils.GsonUtil;
import com.bili.diushoujuaner.utils.entity.dto.CustomSession;
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
    public void getUserLogin(UserAccountReq userAccountReq, final ActionStringCallbackListener<ActionResponse<Void>> actionStringCallbackListener){
        ApiAction.getInstance().getUserLogin(userAccountReq, new ApiStringCallbackListener() {
            @Override
            public void onSuccess(final String data) {
                Tasks.executeInBackground(context, new BackgroundWork<ActionResponse<Void>>() {
                @Override
                public ActionResponse<Void> doInBackground() throws Exception {
                    ApiResponse<CustomSession> result = GsonUtil.getInstance().fromJson(data, new TypeToken<ApiResponse<CustomSession>>() {
                    }.getType());
                    if(result.isLegal()){
                        CustomSessionPreference.getInstance().saveCustomSession(result.getData());
                    }
                    return ActionResponse.getActionRespon(result.getMessage(), result.getRetCode(), (Void)null);
                }
            }, new Completion<ActionResponse<Void>>() {
                @Override
                public void onSuccess(Context context, ActionResponse<Void> result) {
                    actionStringCallbackListener.onSuccess(result);
                }

                @Override
                public void onError(Context context, Exception e) {
                    actionStringCallbackListener.onSuccess(ActionResponse.<Void>getActionResponError());
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
