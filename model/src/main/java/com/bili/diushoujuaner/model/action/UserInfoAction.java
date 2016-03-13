package com.bili.diushoujuaner.model.action;

import android.content.Context;
import android.widget.Toast;

import com.bili.diushoujuaner.model.apihelper.ApiRespon;
import com.bili.diushoujuaner.model.apihelper.api.ApiAction;
import com.bili.diushoujuaner.model.apihelper.callback.ApiCallbackListener;
import com.bili.diushoujuaner.model.callback.ActionCallbackListener;
import com.bili.diushoujuaner.utils.GsonParser;
import com.bili.diushoujuaner.utils.response.UserRes;
import com.google.gson.reflect.TypeToken;
import com.nanotasks.BackgroundWork;
import com.nanotasks.Completion;
import com.nanotasks.Tasks;

/**
 * Created by BiLi on 2016/3/13.
 */
public class UserInfoAction {

    private static UserInfoAction userInfoAction;
    private Context context;

    private UserInfoAction(Context context){
        this.context = context;
    }

    public static UserInfoAction getInstance(Context context){
        if(userInfoAction == null){
            userInfoAction = new UserInfoAction(context);
        }
        return userInfoAction;
    }

    public void getUserInfo(final ActionCallbackListener<ApiRespon<UserRes>> actionCallbackListener){
        ApiAction.getInstance().getUserInfo(new ApiCallbackListener() {
            @Override
            public void onSuccess(final String data) {
                Tasks.executeInBackground(context, new BackgroundWork<ApiRespon<UserRes>>() {
                    @Override
                    public ApiRespon<UserRes> doInBackground() throws Exception {
                        return GsonParser.getInstance().fromJson(data, new TypeToken<ApiRespon<UserRes>>() {
                        }.getType());
                    }
                }, new Completion<ApiRespon<UserRes>>() {
                    @Override
                    public void onSuccess(Context context, ApiRespon<UserRes> result) {
                        actionCallbackListener.onSuccess(result);
                    }

                    @Override
                    public void onError(Context context, Exception e) {
                        Toast.makeText(context,"解析错误",Toast.LENGTH_LONG);
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
