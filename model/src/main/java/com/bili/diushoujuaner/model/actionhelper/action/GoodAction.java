package com.bili.diushoujuaner.model.actionhelper.action;

import android.content.Context;

import com.bili.diushoujuaner.model.actionhelper.IGoodAction;
import com.bili.diushoujuaner.model.actionhelper.respon.ActionRespon;
import com.bili.diushoujuaner.model.apihelper.ApiRespon;
import com.bili.diushoujuaner.model.apihelper.api.ApiAction;
import com.bili.diushoujuaner.model.apihelper.callback.ApiStringCallbackListener;
import com.bili.diushoujuaner.model.callback.ActionStringCallbackListener;
import com.bili.diushoujuaner.utils.GsonUtil;
import com.google.gson.reflect.TypeToken;
import com.nanotasks.BackgroundWork;
import com.nanotasks.Completion;
import com.nanotasks.Tasks;

/**
 * Created by BiLi on 2016/3/23.
 */
public class GoodAction implements IGoodAction {

    private static GoodAction goodAction;
    private Context context;

    public GoodAction(Context context) {
        this.context = context;
    }

    public static synchronized GoodAction getInstance(Context context){
        if(goodAction == null){
            goodAction = new GoodAction(context);
        }
        return goodAction;
    }

    @Override
    public void getGoodAdd(final long recallNo, final ActionStringCallbackListener<ActionRespon<String>> actionStringCallbackListener){
        ApiAction.getInstance().getGoodAdd(recallNo, new ApiStringCallbackListener() {
            @Override
            public void onSuccess(final String data) {
                Tasks.executeInBackground(context, new BackgroundWork<ActionRespon<String>>() {
                    @Override
                    public ActionRespon<String> doInBackground() throws Exception {
                        ApiRespon<String> result = GsonUtil.getInstance().fromJson(data, new TypeToken<ApiRespon<String>>() {
                        }.getType());
                        return ActionRespon.getActionResponFromApiRespon(result);
                    }
                }, new Completion<ActionRespon<String>>() {
                    @Override
                    public void onSuccess(Context context, ActionRespon<String> result) {
                        actionStringCallbackListener.onSuccess(result);
                    }

                    @Override
                    public void onError(Context context, Exception e) {
                        actionStringCallbackListener.onSuccess(ActionRespon.<String>getActionResponError());
                    }
                });
            }

            @Override
            public void onFailure(int errorCode) {
                actionStringCallbackListener.onFailure(errorCode);
            }
        });
    }

    @Override
    public void getGoodRemove(long recallNo, final ActionStringCallbackListener<ActionRespon<String>> actionStringCallbackListener){
        ApiAction.getInstance().getGoodRemove(recallNo, new ApiStringCallbackListener() {
            @Override
            public void onSuccess(final String data) {
                Tasks.executeInBackground(context, new BackgroundWork<ActionRespon<String>>() {
                    @Override
                    public ActionRespon<String> doInBackground() throws Exception {
                        ApiRespon<String> result = GsonUtil.getInstance().fromJson(data, new TypeToken<ApiRespon<String>>() {
                        }.getType());
                        return ActionRespon.getActionResponFromApiRespon(result);
                    }
                }, new Completion<ActionRespon<String>>() {
                    @Override
                    public void onSuccess(Context context, ActionRespon<String> result) {
                        actionStringCallbackListener.onSuccess(result);
                    }

                    @Override
                    public void onError(Context context, Exception e) {
                        actionStringCallbackListener.onSuccess(ActionRespon.<String>getActionResponError());
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
