package com.bili.diushoujuaner.model.apihelper.api;

import android.content.Context;

import com.bili.diushoujuaner.model.apihelper.DataLoader;
import com.bili.diushoujuaner.model.apihelper.callback.ApiCallbackListener;
import com.bili.diushoujuaner.utils.resquest.UserAccountDto;
import com.bili.diushoujuaner.utils.Common;

/**
 * Created by BiLi on 2016/3/11.
 */
public class ApiAction implements Api {

    private static DataLoader dataLoader;
    private static Context mContext;
    private static ApiAction apiAction;

    public static void initialize(Context context) {
        mContext = context;
        dataLoader = new DataLoader(context);
        apiAction = new ApiAction();
    }

    public static synchronized ApiAction getInstance(){
        if(apiAction == null){
            throw new NullPointerException("ApiAction was not initialized!");
        }
        return apiAction;
    }

    @Override
    public void getUserLogin(UserAccountDto userAccountDto, ApiCallbackListener apiCallbackListener) {
        dataLoader.processJsonObjectRequest(Api.userLogin, Common.ConvertObjToMap(userAccountDto),apiCallbackListener);
    }
}
