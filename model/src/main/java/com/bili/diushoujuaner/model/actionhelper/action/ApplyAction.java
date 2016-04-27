package com.bili.diushoujuaner.model.actionhelper.action;

import android.content.Context;

import com.bili.diushoujuaner.model.actionhelper.IApplyAction;
import com.bili.diushoujuaner.model.actionhelper.respon.ActionResponse;
import com.bili.diushoujuaner.model.apihelper.ApiResponse;
import com.bili.diushoujuaner.model.apihelper.api.ApiAction;
import com.bili.diushoujuaner.model.apihelper.callback.ApiStringCallbackListener;
import com.bili.diushoujuaner.model.apihelper.request.FriendApplyReq;
import com.bili.diushoujuaner.model.apihelper.request.FriendAgreeReq;
import com.bili.diushoujuaner.model.apihelper.request.PartyApplyAgreeReq;
import com.bili.diushoujuaner.model.apihelper.request.PartyApplyReq;
import com.bili.diushoujuaner.model.cachehelper.ACache;
import com.bili.diushoujuaner.model.callback.ActionStringCallbackListener;
import com.bili.diushoujuaner.model.databasehelper.DBManager;
import com.bili.diushoujuaner.model.eventhelper.UpdateContactEvent;
import com.bili.diushoujuaner.model.preferhelper.CustomSessionPreference;
import com.bili.diushoujuaner.utils.ConstantUtil;
import com.bili.diushoujuaner.utils.GsonUtil;
import com.bili.diushoujuaner.utils.entity.dto.ContactDto;
import com.bili.diushoujuaner.utils.entity.po.Friend;
import com.bili.diushoujuaner.utils.entity.vo.ApplyVo;
import com.google.gson.reflect.TypeToken;
import com.nanotasks.BackgroundWork;
import com.nanotasks.Completion;
import com.nanotasks.Tasks;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by BiLi on 2016/4/24.
 */
public class ApplyAction implements IApplyAction {

    private static ApplyAction applyAction;
    private Context context;

    public ApplyAction(Context context) {
        this.context = context;
    }

    public static synchronized ApplyAction getInstance(Context context){
        if(applyAction == null){
            applyAction = new ApplyAction(context);
        }
        return applyAction;
    }

    @Override
    public void getPartyApplyAgree(final PartyApplyAgreeReq partyApplyAgreeReq, final ActionStringCallbackListener<ActionResponse<Void>> actionStringCallbackListener) {
        ApiAction.getInstance().getPartyApplyAgree(partyApplyAgreeReq, new ApiStringCallbackListener() {
            @Override
            public void onSuccess(final String data) {
                Tasks.executeInBackground(context, new BackgroundWork<ActionResponse<Void>>() {
                    @Override
                    public ActionResponse<Void> doInBackground() throws Exception {
                        ApiResponse<Void> result = GsonUtil.getInstance().fromJson(data, new TypeToken<ApiResponse<Void>>() {
                        }.getType());
                        if(result.isLegal()){
                            DBManager.getInstance().updateApplyPartyAccept(partyApplyAgreeReq.getPartyNo(), partyApplyAgreeReq.getMemberNo());
                        }
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

    @Override
    public void getFriendApplyAgree(final FriendAgreeReq friendAgreeReq, final ActionStringCallbackListener<ActionResponse<Void>> actionStringCallbackListener) {
        ApiAction.getInstance().getFriendAgree(friendAgreeReq, new ApiStringCallbackListener() {
            @Override
            public void onSuccess(final String data) {
                Tasks.executeInBackground(context, new BackgroundWork<ActionResponse<Void>>() {
                    @Override
                    public ActionResponse<Void> doInBackground() throws Exception {
                        ApiResponse<ContactDto> result = GsonUtil.getInstance().fromJson(data, new TypeToken<ApiResponse<ContactDto>>() {
                        }.getType());
                        if(result.isLegal()){
                            Friend friend = new Friend();
                            friend.setRecent(false);
                            friend.setOwnerNo(CustomSessionPreference.getInstance().getCustomSession().getUserNo());
                            friend.setFriendNo(result.getData().getContNo());
                            friend.setRemark(result.getData().getDisplayName());
                            DBManager.getInstance().saveFriend(friend);
                            DBManager.getInstance().updateApplyFriendAccept(friendAgreeReq.getFriendNo());
                        }
                        return ActionResponse.getActionRespon(result.getMessage(), result.getRetCode(), null);
                    }
                }, new Completion<ActionResponse<Void>>() {
                    @Override
                    public void onSuccess(Context context, ActionResponse<Void> result) {
                        ACache.getInstance().put(ConstantUtil.ACACHE_LAST_TIME_CONTACT, "");
                        EventBus.getDefault().post(new UpdateContactEvent());
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

    @Override
    public void updateApplyRead(final ActionStringCallbackListener<ActionResponse<Void>> actionStringCallbackListener) {
        Tasks.executeInBackground(context, new BackgroundWork<ActionResponse<Void>>() {
            @Override
            public ActionResponse<Void> doInBackground() throws Exception {
                DBManager.getInstance().updateApplyRead();
                return ActionResponse.getActionRespon(null);
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
    public void getApplyVoList(final ActionStringCallbackListener<ActionResponse<List<ApplyVo>>> actionStringCallbackListener) {
        Tasks.executeInBackground(context, new BackgroundWork<ActionResponse<List<ApplyVo>>>() {
            @Override
            public ActionResponse<List<ApplyVo>> doInBackground() throws Exception {
                return ActionResponse.getActionRespon(DBManager.getInstance().getApplyVoList());
            }
        }, new Completion<ActionResponse<List<ApplyVo>>>() {
            @Override
            public void onSuccess(Context context, ActionResponse<List<ApplyVo>> result) {
                actionStringCallbackListener.onSuccess(result);
            }

            @Override
            public void onError(Context context, Exception e) {
                actionStringCallbackListener.onSuccess(ActionResponse.<List<ApplyVo>>getActionResponError());
            }
        });
    }

    @Override
    public void getPartyApply(PartyApplyReq partyApplyReq, final ActionStringCallbackListener<ActionResponse<Void>> actionStringCallbackListener) {
        ApiAction.getInstance().getPartyApply(partyApplyReq, new ApiStringCallbackListener() {
            @Override
            public void onSuccess(final String data) {
                Tasks.executeInBackground(context, new BackgroundWork<ActionResponse<Void>>() {
                    @Override
                    public ActionResponse<Void> doInBackground() throws Exception {
                        ApiResponse<Void> result = GsonUtil.getInstance().fromJson(data, new TypeToken<ApiResponse<Void>>(){}.getType());
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

    @Override
    public void getFriendApply(FriendApplyReq friendApplyReq, final ActionStringCallbackListener<ActionResponse<Void>> actionStringCallbackListener) {
        ApiAction.getInstance().getFriendApply(friendApplyReq, new ApiStringCallbackListener() {
            @Override
            public void onSuccess(final String data) {
                Tasks.executeInBackground(context, new BackgroundWork<ActionResponse<Void>>() {
                    @Override
                    public ActionResponse<Void> doInBackground() throws Exception {
                        ApiResponse<Void> result = GsonUtil.getInstance().fromJson(data, new TypeToken<ApiResponse<Void>>(){}.getType());
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

    @Override
    public void getAddUnReadCount(final ActionStringCallbackListener<ActionResponse<Integer>> actionStringCallbackListener) {
        Tasks.executeInBackground(context, new BackgroundWork<ActionResponse<Integer>>() {
            @Override
            public ActionResponse<Integer> doInBackground() throws Exception {
                return ActionResponse.getActionRespon(DBManager.getInstance().getAddUnReadCount());
            }
        }, new Completion<ActionResponse<Integer>>() {
            @Override
            public void onSuccess(Context context, ActionResponse<Integer> result) {
                actionStringCallbackListener.onSuccess(result);
            }

            @Override
            public void onError(Context context, Exception e) {
                actionStringCallbackListener.onSuccess(ActionResponse.<Integer>getActionResponError());
            }
        });
    }
}
