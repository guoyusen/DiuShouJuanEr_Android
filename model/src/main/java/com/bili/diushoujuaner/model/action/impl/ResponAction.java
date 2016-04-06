package com.bili.diushoujuaner.model.action.impl;

import android.content.Context;

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
import com.nanotasks.BackgroundWork;
import com.nanotasks.Completion;
import com.nanotasks.Tasks;

/**
 * Created by BiLi on 2016/4/2.
 */
public class ResponAction implements IResponAction {

    private static ResponAction responAction;
    private Context context;

    public ResponAction(Context context) {
        this.context = context;
    }

    public static synchronized ResponAction getInstance(Context context){
        if(responAction == null){
            responAction = new ResponAction(context);
        }
        return responAction;
    }

    @Override
    public void getResponAdd(ResponAddReq responAddReq, final ActionCallbackListener<ActionRespon<ResponDto>> actionCallbackListener) {
        ApiAction.getInstance().getResponAdd(responAddReq, new ApiCallbackListener() {
            @Override
            public void onSuccess(final String data) {
                Tasks.executeInBackground(context, new BackgroundWork<ActionRespon<ResponDto>>() {
                    @Override
                    public ActionRespon<ResponDto> doInBackground() throws Exception {
                        ApiRespon<ResponDto> result = GsonParser.getInstance().fromJson(data, new TypeToken<ApiRespon<ResponDto>>() {
                        }.getType());
                        return ActionRespon.getActionResponFromApiRespon(result);
                    }
                }, new Completion<ActionRespon<ResponDto>>() {
                    @Override
                    public void onSuccess(Context context, ActionRespon<ResponDto> result) {
                        actionCallbackListener.onSuccess(result);
                    }

                    @Override
                    public void onError(Context context, Exception e) {
                        actionCallbackListener.onSuccess(ActionRespon.<ResponDto>getActionResponError());
                    }
                });
            }

            @Override
            public void onFailure(int errorCode) {
                actionCallbackListener.onFailure(errorCode);
            }
        });
    }

    @Override
    public void getResponRemove(ResponRemoveReq responRemoveReq, final ActionCallbackListener<ActionRespon<Long>> actionCallbackListener) {
        ApiAction.getInstance().getResponRemove(responRemoveReq, new ApiCallbackListener() {
            @Override
            public void onSuccess(final String data) {
                Tasks.executeInBackground(context, new BackgroundWork<ActionRespon<Long>>() {
                    @Override
                    public ActionRespon<Long> doInBackground() throws Exception {
                        ApiRespon<Long> result = GsonParser.getInstance().fromJson(data, new TypeToken<ApiRespon<Long>>() {
                        }.getType());
                        return ActionRespon.getActionResponFromApiRespon(result);
                    }
                }, new Completion<ActionRespon<Long>>() {
                    @Override
                    public void onSuccess(Context context, ActionRespon<Long> result) {
                        actionCallbackListener.onSuccess(result);
                    }

                    @Override
                    public void onError(Context context, Exception e) {
                        actionCallbackListener.onSuccess(ActionRespon.<Long>getActionResponError());
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
