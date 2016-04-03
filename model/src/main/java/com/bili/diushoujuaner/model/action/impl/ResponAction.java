package com.bili.diushoujuaner.model.action.impl;

import com.bili.diushoujuaner.model.action.IResponAction;
import com.bili.diushoujuaner.model.action.respon.ActionRespon;
import com.bili.diushoujuaner.model.apihelper.ApiRespon;
import com.bili.diushoujuaner.model.apihelper.api.ApiAction;
import com.bili.diushoujuaner.model.apihelper.callback.ApiCallbackListener;
import com.bili.diushoujuaner.model.apihelper.request.ResponAddReq;
import com.bili.diushoujuaner.model.apihelper.request.ResponRemoveReq;
import com.bili.diushoujuaner.model.apihelper.response.ResponDto;
import com.bili.diushoujuaner.model.callback.ActionCallbackListener;
import com.bili.diushoujuaner.utils.GsonParser;
import com.google.gson.reflect.TypeToken;

/**
 * Created by BiLi on 2016/4/2.
 */
public class ResponAction implements IResponAction {

    private static ResponAction responAction;

    public static synchronized ResponAction getInstance(){
        if(responAction == null){
            responAction = new ResponAction();
        }
        return responAction;
    }

    @Override
    public void getResponAdd(ResponAddReq responAddReq, final ActionCallbackListener<ActionRespon<ResponDto>> actionCallbackListener) {
        ApiAction.getInstance().getResponAdd(responAddReq, new ApiCallbackListener() {
            @Override
            public void onSuccess(String data) {
                ApiRespon<ResponDto> result = GsonParser.getInstance().fromJson(data, new TypeToken<ApiRespon<ResponDto>>() {
                }.getType());
                actionCallbackListener.onSuccess(ActionRespon.getActionRespon(result.getMessage(),result.getRetCode(),result.getData()));
            }

            @Override
            public void onFailure(int errorCode) {

            }
        });
    }

    @Override
    public void getResponRemove(ResponRemoveReq responRemoveReq, final ActionCallbackListener<ActionRespon<Long>> actionCallbackListener) {
        ApiAction.getInstance().getResponRemove(responRemoveReq, new ApiCallbackListener() {
            @Override
            public void onSuccess(String data) {
                ApiRespon<Long> result = GsonParser.getInstance().fromJson(data, new TypeToken<ApiRespon<Long>>() {
                }.getType());
                actionCallbackListener.onSuccess(ActionRespon.getActionRespon(result.getMessage(),result.getRetCode(),result.getData()));
            }

            @Override
            public void onFailure(int errorCode) {
                actionCallbackListener.onFailure(errorCode);
            }
        });
    }
}
