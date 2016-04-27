package com.bili.diushoujuaner.model.actionhelper.action;

import android.content.Context;

import com.bili.diushoujuaner.model.actionhelper.IFeedBackAction;
import com.bili.diushoujuaner.model.actionhelper.respon.ActionResponse;
import com.bili.diushoujuaner.model.apihelper.ApiResponse;
import com.bili.diushoujuaner.model.apihelper.api.ApiAction;
import com.bili.diushoujuaner.model.apihelper.callback.ApiStringCallbackListener;
import com.bili.diushoujuaner.model.apihelper.request.FeedBackReq;
import com.bili.diushoujuaner.model.callback.ActionStringCallbackListener;
import com.bili.diushoujuaner.utils.GsonUtil;
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
    public void getFeedBackAdd(FeedBackReq feedBackReq, final ActionStringCallbackListener<ActionResponse<Void>> actionStringCallbackListener) {
        ApiAction.getInstance().getFeedBackAdd(feedBackReq, new ApiStringCallbackListener() {
            @Override
            public void onSuccess(final String data) {
                Tasks.executeInBackground(context, new BackgroundWork<ActionResponse<Void>>() {
                    @Override
                    public ActionResponse<Void> doInBackground() throws Exception {
                        ApiResponse<Void> result = GsonUtil.getInstance().fromJson(data, new TypeToken<ApiResponse<Void>>() {
                        }.getType());
                        return ActionResponse.getActionResponFromApiRespon(result);
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
