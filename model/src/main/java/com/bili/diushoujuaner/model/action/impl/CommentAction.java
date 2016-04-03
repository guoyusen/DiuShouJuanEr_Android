package com.bili.diushoujuaner.model.action.impl;

import com.bili.diushoujuaner.model.action.ICommentAction;
import com.bili.diushoujuaner.model.action.respon.ActionRespon;
import com.bili.diushoujuaner.model.apihelper.ApiRespon;
import com.bili.diushoujuaner.model.apihelper.api.ApiAction;
import com.bili.diushoujuaner.model.apihelper.callback.ApiCallbackListener;
import com.bili.diushoujuaner.model.apihelper.request.CommentAddReq;
import com.bili.diushoujuaner.model.apihelper.request.CommentRemoveReq;
import com.bili.diushoujuaner.model.apihelper.response.CommentDto;
import com.bili.diushoujuaner.model.callback.ActionCallbackListener;
import com.bili.diushoujuaner.utils.GsonParser;
import com.google.gson.reflect.TypeToken;

/**
 * Created by BiLi on 2016/4/2.
 */
public class CommentAction implements ICommentAction {

    private static CommentAction commentAction;

    public static synchronized CommentAction getInstance(){
        if(commentAction == null){
            commentAction = new CommentAction();
        }
        return commentAction;
    }

    @Override
    public void getCommentAdd(CommentAddReq commentAddReq, final ActionCallbackListener<ActionRespon<CommentDto>> actionCallbackListener) {
        ApiAction.getInstance().getCommentAdd(commentAddReq, new ApiCallbackListener() {
            @Override
            public void onSuccess(String data) {
                ApiRespon<CommentDto> result = GsonParser.getInstance().fromJson(data, new TypeToken<ApiRespon<CommentDto>>() {
                }.getType());
                actionCallbackListener.onSuccess(ActionRespon.getActionRespon(result.getMessage(),result.getRetCode(),result.getData()));
            }

            @Override
            public void onFailure(int errorCode) {
                actionCallbackListener.onFailure(errorCode);
            }
        });
    }

    @Override
    public void getCommentRemove(CommentRemoveReq commentRemoveReq, final ActionCallbackListener<ActionRespon<Long>> actionCallbackListener) {
        ApiAction.getInstance().getCommentRemove(commentRemoveReq, new ApiCallbackListener() {
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
