package com.bili.diushoujuaner.model.actionhelper.action;

import android.content.Context;

import com.bili.diushoujuaner.model.actionhelper.IApplyAction;
import com.bili.diushoujuaner.model.actionhelper.respon.ActionRespon;
import com.bili.diushoujuaner.model.apihelper.ApiRespon;
import com.bili.diushoujuaner.model.apihelper.api.ApiAction;
import com.bili.diushoujuaner.model.apihelper.callback.ApiStringCallbackListener;
import com.bili.diushoujuaner.model.apihelper.request.FriendAddReq;
import com.bili.diushoujuaner.model.apihelper.request.FriendAgreeReq;
import com.bili.diushoujuaner.model.cachehelper.ACache;
import com.bili.diushoujuaner.model.callback.ActionStringCallbackListener;
import com.bili.diushoujuaner.model.databasehelper.DBManager;
import com.bili.diushoujuaner.model.eventhelper.AddContactEvent;
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
    public void getFriendAgree(final FriendAgreeReq friendAgreeReq, final ActionStringCallbackListener<ActionRespon<Void>> actionStringCallbackListener) {
        ApiAction.getInstance().getFriendAgree(friendAgreeReq, new ApiStringCallbackListener() {
            @Override
            public void onSuccess(final String data) {
                Tasks.executeInBackground(context, new BackgroundWork<ActionRespon<Void>>() {
                    @Override
                    public ActionRespon<Void> doInBackground() throws Exception {
                        ApiRespon<ContactDto> result = GsonUtil.getInstance().fromJson(data, new TypeToken<ApiRespon<ContactDto>>() {
                        }.getType());
                        if(result.getIsLegal()){
                            Friend friend = new Friend();
                            friend.setRecent(false);
                            friend.setOwnerNo(CustomSessionPreference.getInstance().getCustomSession().getUserNo());
                            friend.setFriendNo(result.getData().getContNo());
                            friend.setRemark(result.getData().getDisplayName());
                            DBManager.getInstance().saveFriend(friend);
                            DBManager.getInstance().updateApplyFriendAccept(friendAgreeReq.getFriendNo());
                        }
                        return ActionRespon.getActionRespon(result.getMessage(), result.getRetCode(), null);
                    }
                }, new Completion<ActionRespon<Void>>() {
                    @Override
                    public void onSuccess(Context context, ActionRespon<Void> result) {
                        ACache.getInstance().put(ConstantUtil.ACACHE_LAST_TIME_CONTACT, "");
                        EventBus.getDefault().post(new AddContactEvent());
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

    @Override
    public void updateApplyRead(final ActionStringCallbackListener<ActionRespon<Void>> actionStringCallbackListener) {
        Tasks.executeInBackground(context, new BackgroundWork<ActionRespon<Void>>() {
            @Override
            public ActionRespon<Void> doInBackground() throws Exception {
                DBManager.getInstance().updateApplyRead();
                return ActionRespon.getActionRespon(null);
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
    public void getApplyVoList(final ActionStringCallbackListener<ActionRespon<List<ApplyVo>>> actionStringCallbackListener) {
        Tasks.executeInBackground(context, new BackgroundWork<ActionRespon<List<ApplyVo>>>() {
            @Override
            public ActionRespon<List<ApplyVo>> doInBackground() throws Exception {
                return ActionRespon.getActionRespon(DBManager.getInstance().getApplyVoList());
            }
        }, new Completion<ActionRespon<List<ApplyVo>>>() {
            @Override
            public void onSuccess(Context context, ActionRespon<List<ApplyVo>> result) {
                actionStringCallbackListener.onSuccess(result);
            }

            @Override
            public void onError(Context context, Exception e) {
                actionStringCallbackListener.onSuccess(ActionRespon.<List<ApplyVo>>getActionResponError());
            }
        });
    }

    @Override
    public void getFriendAdd(FriendAddReq friendAddReq, final ActionStringCallbackListener<ActionRespon<Void>> actionStringCallbackListener) {
        ApiAction.getInstance().getFriendAdd(friendAddReq, new ApiStringCallbackListener() {
            @Override
            public void onSuccess(final String data) {
                Tasks.executeInBackground(context, new BackgroundWork<ActionRespon<Void>>() {
                    @Override
                    public ActionRespon<Void> doInBackground() throws Exception {
                        ApiRespon<Void> result = GsonUtil.getInstance().fromJson(data, new TypeToken<ApiRespon<Void>>(){}.getType());
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

    @Override
    public void getAddUnReadCount(final ActionStringCallbackListener<ActionRespon<Integer>> actionStringCallbackListener) {
        Tasks.executeInBackground(context, new BackgroundWork<ActionRespon<Integer>>() {
            @Override
            public ActionRespon<Integer> doInBackground() throws Exception {
                return ActionRespon.getActionRespon(DBManager.getInstance().getAddUnReadCount());
            }
        }, new Completion<ActionRespon<Integer>>() {
            @Override
            public void onSuccess(Context context, ActionRespon<Integer> result) {
                actionStringCallbackListener.onSuccess(result);
            }

            @Override
            public void onError(Context context, Exception e) {
                actionStringCallbackListener.onSuccess(ActionRespon.<Integer>getActionResponError());
            }
        });
    }
}
