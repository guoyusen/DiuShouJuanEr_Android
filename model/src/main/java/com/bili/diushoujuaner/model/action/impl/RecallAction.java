package com.bili.diushoujuaner.model.action.impl;

import android.content.Context;

import com.bili.diushoujuaner.model.action.IRecallAction;
import com.bili.diushoujuaner.model.action.respon.ActionRespon;
import com.bili.diushoujuaner.model.apihelper.ApiRespon;
import com.bili.diushoujuaner.model.apihelper.api.ApiAction;
import com.bili.diushoujuaner.model.apihelper.callback.ApiCallbackListener;
import com.bili.diushoujuaner.model.apihelper.request.RecentRecallReq;
import com.bili.diushoujuaner.model.cachehelper.ACache;
import com.bili.diushoujuaner.utils.Constant;
import com.bili.diushoujuaner.utils.GsonParser;
import com.bili.diushoujuaner.model.apihelper.request.RecallListReq;
import com.bili.diushoujuaner.model.apihelper.response.RecallDto;
import com.bili.diushoujuaner.model.callback.ActionCallbackListener;
import com.google.gson.reflect.TypeToken;
import com.nanotasks.BackgroundWork;
import com.nanotasks.Completion;
import com.nanotasks.Tasks;

import java.util.List;

/**
 * Created by BiLi on 2016/3/10.
 */
public class RecallAction implements IRecallAction {

    private static RecallAction recallAction;
    private Context context;

    public RecallAction(Context context) {
        this.context = context;
    }

    public static synchronized RecallAction getInstance(Context context){
        if(recallAction == null){
            recallAction = new RecallAction(context);
        }
        return recallAction;
    }

    @Override
    public void getRecentRecall(final RecentRecallReq recentRecallReq, final ActionCallbackListener<ActionRespon<RecallDto>> actionCallbackListener) {
        //获取十分钟内的缓存
        RecallDto recallDto = GsonParser.getInstance().fromJson(ACache.getInstance().getAsString("RECENT_RECALL_" + recentRecallReq.getUserNo()), new TypeToken<RecallDto>() {
        }.getType());
        if(recallDto != null){
            actionCallbackListener.onSuccess(ActionRespon.getActionRespon(Constant.ACTION_LOAD_LOCAL_SUCCESS,Constant.RETCODE_SUCCESS,recallDto));
        }else{
            ApiAction.getInstance().getRecentRecall(recentRecallReq, new ApiCallbackListener() {
                @Override
                public void onSuccess(String data) {
                    ApiRespon<RecallDto> result = GsonParser.getInstance().fromJson(data, new TypeToken<ApiRespon<RecallDto>>() {
                    }.getType());
                    if(result.getIsLegal()){
                        ACache.getInstance().put("RECENT_RECALL_" + recentRecallReq.getUserNo(),GsonParser.getInstance().toJson(result.getData()),Constant.ACACHE_TIME_RECENT_RECALL);
                    }
                    actionCallbackListener.onSuccess(ActionRespon.getActionRespon(result.getMessage(),result.getRetCode(),result.getData()));
                }

                @Override
                public void onFailure(int errorCode) {
                    actionCallbackListener.onFailure(errorCode);
                }
            });
        }
    }

    @Override
    public void getRecallListFromACache(final ActionCallbackListener<ActionRespon<List<RecallDto>>> actionCallbackListener){
        Tasks.executeInBackground(context, new BackgroundWork<ActionRespon<List<RecallDto>>>() {
            @Override
            public ActionRespon<List<RecallDto>> doInBackground() throws Exception {
                List<RecallDto> recallDtoList = GsonParser.getInstance().fromJson(ACache.getInstance().getAsString(Constant.ACACHE_RECALL_LIST), new TypeToken<List<RecallDto>>() {
                }.getType());
                return ActionRespon.getActionRespon(recallDtoList);
            }
        }, new Completion<ActionRespon<List<RecallDto>>>() {
            @Override
            public void onSuccess(Context context, ActionRespon<List<RecallDto>> result) {
                actionCallbackListener.onSuccess(result);
            }

            @Override
            public void onError(Context context, Exception e) {
                actionCallbackListener.onSuccess(ActionRespon.<List<RecallDto>>getActionResponError());
            }
        });
    }

    @Override
    public void getRecallList(RecallListReq recallListReq, final ActionCallbackListener<ActionRespon<List<RecallDto>>> actionCallbackListener){
        ApiAction.getInstance().getRecallList(recallListReq, new ApiCallbackListener() {
            @Override
            public void onSuccess(final String data) {
                Tasks.executeInBackground(context, new BackgroundWork<ActionRespon<List<RecallDto>>>() {
                    @Override
                    public ActionRespon<List<RecallDto>> doInBackground() throws Exception {
                        ApiRespon<List<RecallDto>> result = GsonParser.getInstance().fromJson(data, new TypeToken<ApiRespon<List<RecallDto>>>() {
                        }.getType());
                        return ActionRespon.getActionResponFromApiRespon(result);
                    }
                }, new Completion<ActionRespon<List<RecallDto>>>() {
                    @Override
                    public void onSuccess(Context context, ActionRespon<List<RecallDto>> result) {
                        actionCallbackListener.onSuccess(result);
                    }

                    @Override
                    public void onError(Context context, Exception e) {
                        actionCallbackListener.onSuccess(ActionRespon.<List<RecallDto>>getActionResponError());
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
