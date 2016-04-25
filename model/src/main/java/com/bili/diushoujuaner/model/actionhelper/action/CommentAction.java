package com.bili.diushoujuaner.model.actionhelper.action;

import android.content.Context;

import com.bili.diushoujuaner.model.actionhelper.ICommentAction;
import com.bili.diushoujuaner.model.actionhelper.respon.ActionRespon;
import com.bili.diushoujuaner.model.apihelper.ApiRespon;
import com.bili.diushoujuaner.model.apihelper.api.ApiAction;
import com.bili.diushoujuaner.model.apihelper.callback.ApiStringCallbackListener;
import com.bili.diushoujuaner.model.apihelper.request.CommentAddReq;
import com.bili.diushoujuaner.model.apihelper.request.CommentRemoveReq;
import com.bili.diushoujuaner.utils.entity.dto.CommentDto;
import com.bili.diushoujuaner.model.callback.ActionStringCallbackListener;
import com.bili.diushoujuaner.utils.GsonUtil;
import com.google.gson.reflect.TypeToken;
import com.nanotasks.BackgroundWork;
import com.nanotasks.Completion;
import com.nanotasks.Tasks;

/**
 * Created by BiLi on 2016/4/2.
 */
public class CommentAction implements ICommentAction {

    private static CommentAction commentAction;
    private Context context;

    public CommentAction(Context context) {
        this.context = context;
    }

    public static synchronized CommentAction getInstance(Context context){
        if(commentAction == null){
            commentAction = new CommentAction(context);
        }
        return commentAction;
    }

    @Override
    public void getCommentAdd(CommentAddReq commentAddReq, final ActionStringCallbackListener<ActionRespon<CommentDto>> actionStringCallbackListener) {
        ApiAction.getInstance().getCommentAdd(commentAddReq, new ApiStringCallbackListener() {
            @Override
            public void onSuccess(final String data) {
                Tasks.executeInBackground(context, new BackgroundWork<ActionRespon<CommentDto>>() {
                    @Override
                    public ActionRespon<CommentDto> doInBackground() throws Exception {
                        ApiRespon<CommentDto> result = GsonUtil.getInstance().fromJson(data, new TypeToken<ApiRespon<CommentDto>>() {
                        }.getType());
                        return ActionRespon.getActionResponFromApiRespon(result);
                    }
                }, new Completion<ActionRespon<CommentDto>>() {
                    @Override
                    public void onSuccess(Context context, ActionRespon<CommentDto> result) {
                        actionStringCallbackListener.onSuccess(result);
                    }

                    @Override
                    public void onError(Context context, Exception e) {
                        actionStringCallbackListener.onSuccess(ActionRespon.<CommentDto>getActionResponError());
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
    public void getCommentRemove(CommentRemoveReq commentRemoveReq, final ActionStringCallbackListener<ActionRespon<Long>> actionStringCallbackListener) {
        ApiAction.getInstance().getCommentRemove(commentRemoveReq, new ApiStringCallbackListener() {
            @Override
            public void onSuccess(final String data) {
                Tasks.executeInBackground(context, new BackgroundWork<ActionRespon<Long>>() {
                    @Override
                    public ActionRespon<Long> doInBackground() throws Exception {
                        ApiRespon<Long> result = GsonUtil.getInstance().fromJson(data, new TypeToken<ApiRespon<Long>>() {
                        }.getType());
                        return ActionRespon.getActionResponFromApiRespon(result);
                    }
                }, new Completion<ActionRespon<Long>>() {
                    @Override
                    public void onSuccess(Context context, ActionRespon<Long> result) {
                        actionStringCallbackListener.onSuccess(result);
                    }

                    @Override
                    public void onError(Context context, Exception e) {
                        actionStringCallbackListener.onSuccess(ActionRespon.<Long>getActionResponError());
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
