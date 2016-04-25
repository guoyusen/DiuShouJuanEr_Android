package com.bili.diushoujuaner.model.actionhelper.action;

import android.content.Context;

import com.bili.diushoujuaner.model.actionhelper.IRecallAction;
import com.bili.diushoujuaner.model.actionhelper.respon.ActionRespon;
import com.bili.diushoujuaner.model.apihelper.ApiRespon;
import com.bili.diushoujuaner.model.apihelper.api.ApiAction;
import com.bili.diushoujuaner.model.apihelper.callback.ApiStringCallbackListener;
import com.bili.diushoujuaner.model.apihelper.request.RecallPublishReq;
import com.bili.diushoujuaner.model.apihelper.request.RecallRemoveReq;
import com.bili.diushoujuaner.model.apihelper.request.RecentRecallReq;
import com.bili.diushoujuaner.model.cachehelper.ACache;
import com.bili.diushoujuaner.model.preferhelper.CustomSessionPreference;
import com.bili.diushoujuaner.utils.ConstantUtil;
import com.bili.diushoujuaner.utils.GsonUtil;
import com.bili.diushoujuaner.model.apihelper.request.RecallListReq;
import com.bili.diushoujuaner.utils.StringUtil;
import com.bili.diushoujuaner.utils.entity.dto.RecallDto;
import com.bili.diushoujuaner.model.callback.ActionStringCallbackListener;
import com.google.gson.reflect.TypeToken;
import com.nanotasks.BackgroundWork;
import com.nanotasks.Completion;
import com.nanotasks.Tasks;

import java.util.ArrayList;
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
    public void getRecallPublish(RecallPublishReq recallPublishReq, final ActionStringCallbackListener<ActionRespon<RecallDto>> actionStringCallbackListener) {
        ApiAction.getInstance().getRecallPublish(recallPublishReq, new ApiStringCallbackListener() {
            @Override
            public void onSuccess(final String data) {
                Tasks.executeInBackground(context, new BackgroundWork<ActionRespon<RecallDto>>() {
                    @Override
                    public ActionRespon<RecallDto> doInBackground() throws Exception {
                        ApiRespon<RecallDto> result = GsonUtil.getInstance().fromJson(data, new TypeToken<ApiRespon<RecallDto>>() {
                        }.getType());
                        //更新space列表缓存
                        String data = ACache.getInstance().getAsString(ConstantUtil.ACACHE_USER_RECALL_PREFIX + CustomSessionPreference.getInstance().getCustomSession().getUserNo());
                        List<RecallDto> recallDtoList = new ArrayList<RecallDto>();
                        if(!StringUtil.isEmpty(data)) {
                            recallDtoList.addAll((List<RecallDto>) GsonUtil.getInstance().fromJson(data, new TypeToken<List<RecallDto>>() {}.getType()));
                        }
                        recallDtoList.add(0, result.getData());
                        ACache.getInstance().put(ConstantUtil.ACACHE_USER_RECALL_PREFIX + CustomSessionPreference.getInstance().getCustomSession().getUserNo(), GsonUtil.getInstance().toJson(recallDtoList));

                        return ActionRespon.getActionResponFromApiRespon(result);
                    }
                }, new Completion<ActionRespon<RecallDto>>() {
                    @Override
                    public void onSuccess(Context context, ActionRespon<RecallDto> result) {
                        actionStringCallbackListener.onSuccess(result);
                    }

                    @Override
                    public void onError(Context context, Exception e) {
                        actionStringCallbackListener.onSuccess(ActionRespon.<RecallDto>getActionResponError());
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
    public void getRecallRemove(RecallRemoveReq recallRemoveReq, final ActionStringCallbackListener<ActionRespon<Long>> actionStringCallbackListener) {
        ApiAction.getInstance().getRecallRemove(recallRemoveReq, new ApiStringCallbackListener() {
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
                        actionStringCallbackListener.onSuccess(ActionRespon.getActionRespon(Long.parseLong("-1")));
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
    public void getRecentRecall(final RecentRecallReq recentRecallReq, final ActionStringCallbackListener<ActionRespon<RecallDto>> actionStringCallbackListener) {
        //获取十分钟内的缓存
        RecallDto recallDto = GsonUtil.getInstance().fromJson(ACache.getInstance().getAsString(ConstantUtil.ACACHE_RECENT_RECALL_PREFIX + recentRecallReq.getUserNo()), new TypeToken<RecallDto>() {
        }.getType());
        if(recallDto != null){
            actionStringCallbackListener.onSuccess(ActionRespon.getActionRespon(ConstantUtil.ACTION_LOAD_LOCAL_SUCCESS, ConstantUtil.RETCODE_SUCCESS,recallDto));
        }else{
            ApiAction.getInstance().getRecentRecall(recentRecallReq, new ApiStringCallbackListener() {
                @Override
                public void onSuccess(String data) {
                    ApiRespon<RecallDto> result = GsonUtil.getInstance().fromJson(data, new TypeToken<ApiRespon<RecallDto>>() {
                    }.getType());
                    if(result.getIsLegal()){
                        ACache.getInstance().put(ConstantUtil.ACACHE_RECENT_RECALL_PREFIX + recentRecallReq.getUserNo(), GsonUtil.getInstance().toJson(result.getData()), ConstantUtil.ACACHE_TIME_RECENT_RECALL);
                    }
                    actionStringCallbackListener.onSuccess(ActionRespon.getActionRespon(result.getMessage(),result.getRetCode(),result.getData()));
                }

                @Override
                public void onFailure(int errorCode) {
                    actionStringCallbackListener.onFailure(errorCode);
                }
            });
        }
    }

    @Override
    public void getUserRecallListFromACache(final long userNo, final ActionStringCallbackListener<ActionRespon<List<RecallDto>>> actionStringCallbackListener) {
        Tasks.executeInBackground(context, new BackgroundWork<ActionRespon<List<RecallDto>>>() {
            @Override
            public ActionRespon<List<RecallDto>> doInBackground() throws Exception {
                String data = ACache.getInstance().getAsString(ConstantUtil.ACACHE_USER_RECALL_PREFIX + userNo);
                if(!StringUtil.isEmpty(data)){
                    List<RecallDto> recallDtoList = GsonUtil.getInstance().fromJson(data, new TypeToken<List<RecallDto>>() {
                    }.getType());
                    return ActionRespon.getActionRespon(recallDtoList);
                }
                return ActionRespon.getActionRespon((List<RecallDto>)null);
            }
        }, new Completion<ActionRespon<List<RecallDto>>>() {
            @Override
            public void onSuccess(Context context, ActionRespon<List<RecallDto>> result) {
                actionStringCallbackListener.onSuccess(result);
            }

            @Override
            public void onError(Context context, Exception e) {
                actionStringCallbackListener.onSuccess(ActionRespon.<List<RecallDto>>getActionResponError());
            }
        });
    }

    @Override
    public void getRecallListFromACache(final ActionStringCallbackListener<ActionRespon<List<RecallDto>>> actionStringCallbackListener){
        Tasks.executeInBackground(context, new BackgroundWork<ActionRespon<List<RecallDto>>>() {
            @Override
            public ActionRespon<List<RecallDto>> doInBackground() throws Exception {
                List<RecallDto> recallDtoList = GsonUtil.getInstance().fromJson(ACache.getInstance().getAsString(ConstantUtil.ACACHE_RECALL_LIST), new TypeToken<List<RecallDto>>() {
                }.getType());
                return ActionRespon.getActionRespon(recallDtoList);
            }
        }, new Completion<ActionRespon<List<RecallDto>>>() {
            @Override
            public void onSuccess(Context context, ActionRespon<List<RecallDto>> result) {
                actionStringCallbackListener.onSuccess(result);
            }

            @Override
            public void onError(Context context, Exception e) {
                actionStringCallbackListener.onSuccess(ActionRespon.<List<RecallDto>>getActionResponError());
            }
        });
    }

    @Override
    public void getRecallList(final RecallListReq recallListReq, final ActionStringCallbackListener<ActionRespon<List<RecallDto>>> actionStringCallbackListener){
        ApiAction.getInstance().getRecallList(recallListReq, new ApiStringCallbackListener() {
            @Override
            public void onSuccess(final String data) {
                Tasks.executeInBackground(context, new BackgroundWork<ActionRespon<List<RecallDto>>>() {
                    @Override
                    public ActionRespon<List<RecallDto>> doInBackground() throws Exception {
                        ApiRespon<List<RecallDto>> result = GsonUtil.getInstance().fromJson(data, new TypeToken<ApiRespon<List<RecallDto>>>() {
                        }.getType());
                        if(recallListReq.getUserNo() == CustomSessionPreference.getInstance().getCustomSession().getUserNo()){
                            //如果是当前用户自己的请求，那么表明用户在查看自己的空间，进行缓存
                            ACache.getInstance().put(ConstantUtil.ACACHE_USER_RECALL_PREFIX + CustomSessionPreference.getInstance().getCustomSession().getUserNo(), GsonUtil.getInstance().toJson(result.getData()));
                        }
                        return ActionRespon.getActionResponFromApiRespon(result);
                    }
                }, new Completion<ActionRespon<List<RecallDto>>>() {
                    @Override
                    public void onSuccess(Context context, ActionRespon<List<RecallDto>> result) {
                        actionStringCallbackListener.onSuccess(result);
                    }

                    @Override
                    public void onError(Context context, Exception e) {
                        actionStringCallbackListener.onSuccess(ActionRespon.<List<RecallDto>>getActionResponError());
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
