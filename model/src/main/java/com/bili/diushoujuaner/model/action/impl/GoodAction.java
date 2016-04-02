package com.bili.diushoujuaner.model.action.impl;

import com.bili.diushoujuaner.model.action.IGoodAction;
import com.bili.diushoujuaner.model.apihelper.ApiRespon;
import com.bili.diushoujuaner.model.apihelper.api.ApiAction;
import com.bili.diushoujuaner.model.apihelper.callback.ApiCallbackListener;
import com.bili.diushoujuaner.model.callback.ActionCallbackListener;
import com.bili.diushoujuaner.utils.GsonParser;
import com.google.gson.reflect.TypeToken;

/**
 * Created by BiLi on 2016/3/23.
 */
public class GoodAction implements IGoodAction {

    private static GoodAction goodAction;

    public static synchronized GoodAction getInstance(){
        if(goodAction == null){
            goodAction = new GoodAction();
        }
        return goodAction;
    }

    @Override
    public void getGoodAdd(long recallNo, final ActionCallbackListener<ApiRespon<String>> actionCallbackListener){
        ApiAction.getInstance().getGoodAdd(recallNo, new ApiCallbackListener() {
            @Override
            public void onSuccess(String data) {
                ApiRespon<String> result = GsonParser.getInstance().fromJson(data, new TypeToken<ApiRespon<String>>() {
                }.getType());
                actionCallbackListener.onSuccess(result);
            }

            @Override
            public void onFailure(int errorCode) {
                actionCallbackListener.onFailure(errorCode);
            }
        });
    }

    @Override
    public void getGoodRemove(long recallNo, final ActionCallbackListener<ApiRespon<String>> actionCallbackListener){
        ApiAction.getInstance().getGoodRemove(recallNo, new ApiCallbackListener() {
            @Override
            public void onSuccess(String data) {
                ApiRespon<String> result = GsonParser.getInstance().fromJson(data, new TypeToken<ApiRespon<String>>() {
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
