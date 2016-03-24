package com.bili.diushoujuaner.model.apihelper.api;

import android.content.Context;

import com.android.volley.Request;
import com.bili.diushoujuaner.model.apihelper.DataLoader;
import com.bili.diushoujuaner.model.apihelper.callback.ApiCallbackListener;
import com.bili.diushoujuaner.utils.request.RecallListReq;
import com.bili.diushoujuaner.utils.request.UserAccountReq;
import com.bili.diushoujuaner.utils.Common;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by BiLi on 2016/3/11.
 */
public class ApiAction implements Api {

    private static DataLoader dataLoader;
    private static ApiAction apiAction;

    public static void initialize() {
        dataLoader = new DataLoader();
        apiAction = new ApiAction();
    }

    public static synchronized ApiAction getInstance(){
        if(apiAction == null){
            throw new NullPointerException("ApiAction was not initialized!");
        }
        return apiAction;
    }

    @Override
    public void getUserLogin(UserAccountReq userAccountReq, ApiCallbackListener apiCallbackListener) {
        dataLoader.processStringRequest(Request.Method.POST, Api.getUserLogin, Common.ConvertObjToMap(userAccountReq), apiCallbackListener);
    }

    @Override
    public void getUserInfo(ApiCallbackListener apiCallbackListener) {
        dataLoader.processStringRequest(Request.Method.GET, Api.getUserInfo, null, apiCallbackListener);
    }

    @Override
    public void getContactList(ApiCallbackListener apiCallbackListener) {
        dataLoader.processStringRequest(Request.Method.GET, Api.getContactList, null, apiCallbackListener);
    }

    @Override
    public void getRecallList(RecallListReq recallListReq, ApiCallbackListener apiCallbackListener) {
        dataLoader.processStringRequest(Request.Method.GET, Common.getCompleteUrl(Api.getRecallList, recallListReq), null, apiCallbackListener);
    }

    @Override
    public void getGoodAdd(long recallNo, ApiCallbackListener apiCallbackListener) {
        Map<String, String> params = new HashMap<>();
        params.put("recallNo",recallNo + "");

        dataLoader.processStringRequest(Request.Method.POST, Api.getGoodAdd, params, apiCallbackListener);
    }

    @Override
    public void getGoodRemove(long recallNo, ApiCallbackListener apiCallbackListener) {
        Map<String, String> params = new HashMap<>();
        params.put("recallNo",recallNo + "");

        dataLoader.processStringRequest(Request.Method.POST, Api.getGoodRemove, params, apiCallbackListener);
    }
}
