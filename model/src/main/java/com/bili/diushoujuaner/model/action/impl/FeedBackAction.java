package com.bili.diushoujuaner.model.action.impl;

import android.content.Context;

import com.bili.diushoujuaner.model.action.IFeedBackAction;
import com.bili.diushoujuaner.model.action.respon.ActionRespon;
import com.bili.diushoujuaner.model.apihelper.ApiRespon;
import com.bili.diushoujuaner.model.apihelper.api.ApiAction;
import com.bili.diushoujuaner.model.apihelper.callback.ApiStringCallbackListener;
import com.bili.diushoujuaner.model.apihelper.request.FeedBackReq;
import com.bili.diushoujuaner.model.callback.ActionStringCallbackListener;
import com.bili.diushoujuaner.utils.GsonParser;
import com.google.gson.reflect.TypeToken;
import com.nanotasks.BackgroundWork;
import com.nanotasks.Completion;
import com.nanotasks.Tasks;

/**
 * Created by BiLi on 2016/4/6.
 */
public class FeedBackAction implements IFeedBackAction {
    private static FeedBackAction feedBackAction;
    private Context context;

    public FeedBackAction(Context context) {
        this.context = context;
    }

    public static synchronized FeedBackAction getInstance(Context context){
        if(feedBackAction == null){
            feedBackAction = new FeedBackAction(context);
        }
        return feedBackAction;
    }
    @Override
    public void getFeedBackAdd(FeedBackReq feedBackReq, final ActionStringCallbackListener<ActionRespon<Void>> actionStringCallbackListener) {
        ApiAction.getInstance().getFeedBackAdd(feedBackReq, new ApiStringCallbackListener() {
            @Override
            public void onSuccess(final String data) {
                Tasks.executeInBackground(context, new BackgroundWork<ActionRespon<Void>>() {
                    @Override
                    public ActionRespon<Void> doInBackground() throws Exception {
                        ApiRespon<Void> result = GsonParser.getInstance().fromJson(data, new TypeToken<ApiRespon<Void>>() {
                        }.getType());
                        return ActionRespon.getActionResponFromApiRespon(result);
                    }
                }, new Completion<ActionRespon<Void>>() {
                    @Override
                    public void onSuccess(Context context, ActionRespon<Void> result) {
                        actionStringCallbackListener.onSuccess(result);
                    }

                    @Override
                    public void onError(Context context, Exception e) {
                        actionStringCallbackListener.onSuccess(ActionRespon.<Void>getActionResponError());
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
