package com.bili.diushoujuaner.model.action;

import android.content.Context;

import com.bili.diushoujuaner.model.apihelper.ApiRespon;
import com.bili.diushoujuaner.model.apihelper.api.ApiAction;
import com.bili.diushoujuaner.model.apihelper.callback.ApiCallbackListener;
import com.bili.diushoujuaner.utils.GsonParser;
import com.bili.diushoujuaner.utils.request.RecallListReq;
import com.bili.diushoujuaner.utils.response.RecallDto;
import com.bili.diushoujuaner.model.callback.ActionCallbackListener;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * Created by BiLi on 2016/3/10.
 */
public class RecallAction {

    private static RecallAction recallAction;

    public static synchronized RecallAction getInstance(){
        if(recallAction == null){
            recallAction = new RecallAction();
        }
        return recallAction;
    }

    public void getRecallList(RecallListReq recallListReq, final ActionCallbackListener<ApiRespon<List<RecallDto>>> actionCallbackListener){
        ApiAction.getInstance().getRecallList(recallListReq, new ApiCallbackListener() {
            @Override
            public void onSuccess(final String data) {
                ApiRespon<List<RecallDto>> result = GsonParser.getInstance().fromJson(data, new TypeToken<ApiRespon<List<RecallDto>>>() {
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
