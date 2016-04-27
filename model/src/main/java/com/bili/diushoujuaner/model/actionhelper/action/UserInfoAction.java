package com.bili.diushoujuaner.model.actionhelper.action;

import android.content.Context;

import com.bili.diushoujuaner.model.actionhelper.IUserInfoAction;
import com.bili.diushoujuaner.model.actionhelper.respon.ActionResponse;
import com.bili.diushoujuaner.model.apihelper.ApiResponse;
import com.bili.diushoujuaner.model.apihelper.api.ApiAction;
import com.bili.diushoujuaner.model.apihelper.callback.ApiStringCallbackListener;
import com.bili.diushoujuaner.model.apihelper.request.AccountUpdateReq;
import com.bili.diushoujuaner.model.apihelper.request.AutographModifyReq;
import com.bili.diushoujuaner.model.apihelper.request.UserInfoReq;
import com.bili.diushoujuaner.model.apihelper.request.VerifyReq;
import com.bili.diushoujuaner.model.eventhelper.NextPageEvent;
import com.bili.diushoujuaner.model.eventhelper.StartTimerEvent;
import com.bili.diushoujuaner.model.eventhelper.UpdateAutographEvent;
import com.bili.diushoujuaner.model.eventhelper.UpdateUserInfoEvent;
import com.bili.diushoujuaner.utils.StringUtil;
import com.bili.diushoujuaner.utils.TimeUtil;
import com.bili.diushoujuaner.utils.entity.dto.CustomSession;
import com.bili.diushoujuaner.model.callback.ActionStringCallbackListener;
import com.bili.diushoujuaner.model.databasehelper.DBManager;
import com.bili.diushoujuaner.utils.entity.po.User;
import com.bili.diushoujuaner.model.preferhelper.CustomSessionPreference;
import com.bili.diushoujuaner.utils.GsonUtil;
import com.bili.diushoujuaner.utils.entity.dto.UserDto;
import com.google.gson.reflect.TypeToken;
import com.nanotasks.BackgroundWork;
import com.nanotasks.Completion;
import com.nanotasks.Tasks;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by BiLi on 2016/3/13.
 */
public class UserInfoAction implements IUserInfoAction {

    private static UserInfoAction userInfoAction;
    private User user;
    private Context context;

    public UserInfoAction(Context context) {
        this.context = context;
    }

    public static synchronized UserInfoAction getInstance(Context context){
        if(userInfoAction == null){
            userInfoAction = new UserInfoAction(context);
        }
        return userInfoAction;
    }

    @Override
    public void getLogout(final ActionStringCallbackListener<ActionResponse<Void>> actionStringCallbackListener) {
        ApiAction.getInstance().getLogout(new ApiStringCallbackListener() {
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

    @Override
    public void getAcountReset(AccountUpdateReq accountUpdateReq, final ActionStringCallbackListener<ActionResponse<Void>> actionStringCallbackListener) {
        ApiAction.getInstance().getAcountReset(accountUpdateReq, new ApiStringCallbackListener() {
            @Override
            public void onSuccess(String data) {
                processCustomSessionData(data, actionStringCallbackListener);
            }

            @Override
            public void onFailure(int errorCode) {
                actionStringCallbackListener.onFailure(errorCode);
            }
        });
    }


    @Override
    public void getAcountRegist(AccountUpdateReq accountUpdateReq, final ActionStringCallbackListener<ActionResponse<Void>> actionStringCallbackListener) {
        ApiAction.getInstance().getAcountRegist(accountUpdateReq, new ApiStringCallbackListener() {
            @Override
            public void onSuccess(String data) {
                processCustomSessionData(data, actionStringCallbackListener);
            }

            @Override
            public void onFailure(int errorCode) {
                actionStringCallbackListener.onFailure(errorCode);
            }
        });
    }

    private void processCustomSessionData(final String data, final ActionStringCallbackListener<ActionResponse<Void>> actionStringCallbackListener){
        Tasks.executeInBackground(context, new BackgroundWork<ActionResponse<Void>>() {
            @Override
            public ActionResponse<Void> doInBackground() throws Exception {
                ApiResponse<CustomSession> result = GsonUtil.getInstance().fromJson(data, new TypeToken<ApiResponse<CustomSession>>() {
                }.getType());
                if(result.isLegal()){
                    EventBus.getDefault().post(new NextPageEvent(2));
                    CustomSessionPreference.getInstance().saveCustomSession(result.getData());
                }
                return ActionResponse.getActionRespon(result.getMessage(), result.getRetCode(), (Void)null);
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
    public void getVerifyCode(final VerifyReq verifyReq, final ActionStringCallbackListener<ActionResponse<Void>> actionStringCallbackListener) {
        ApiAction.getInstance().getVerifyCode(verifyReq, new ApiStringCallbackListener() {
            @Override
            public void onSuccess(final String data) {
                Tasks.executeInBackground(context, new BackgroundWork<ActionResponse<Void>>() {
                    @Override
                    public ActionResponse<Void> doInBackground() throws Exception {
                        ApiResponse<Void> result = GsonUtil.getInstance().fromJson(data, new TypeToken<ApiResponse<Void>>() {
                        }.getType());
                        if(result.isLegal()){
                            EventBus.getDefault().post((new NextPageEvent(1)).setMobile(verifyReq.getMobile()));
                            EventBus.getDefault().post((new StartTimerEvent()));
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
    public void clearUser() {
        this.user = null;
    }

    @Override
    public void getUserInfoUpdate(UserInfoReq userInfoReq, final ActionStringCallbackListener<ActionResponse<User>> actionStringCallbackListener) {
        ApiAction.getInstance().getUserInfoUpdate(userInfoReq, new ApiStringCallbackListener() {
            @Override
            public void onSuccess(final String data) {
                Tasks.executeInBackground(context, new BackgroundWork<ActionResponse<User>>() {
                    @Override
                    public ActionResponse<User> doInBackground() throws Exception {
                        ApiResponse<UserDto> result = GsonUtil.getInstance().fromJson(data, new TypeToken<ApiResponse<UserDto>>() {
                        }.getType());
                        if(result.isLegal()) {
                            DBManager.getInstance().saveUser(result.getData());
                            user = DBManager.getInstance().getUser(CustomSessionPreference.getInstance().getCustomSession().getUserNo());
                            EventBus.getDefault().post(new UpdateUserInfoEvent());
                        }
                        return ActionResponse.getActionRespon(result.getMessage(), result.getRetCode(), getUserFromLocal());
                    }
                }, new Completion<ActionResponse<User>>() {
                    @Override
                    public void onSuccess(Context context, ActionResponse<User> result) {
                        actionStringCallbackListener.onSuccess(result);
                    }

                    @Override
                    public void onError(Context context, Exception e) {
                        actionStringCallbackListener.onSuccess(ActionResponse.<User>getActionResponError());
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
    public void getAutographModify(AutographModifyReq autographModifyReq, final ActionStringCallbackListener<ActionResponse<String>> actionStringCallbackListener) {
        ApiAction.getInstance().getAutographModify(autographModifyReq, new ApiStringCallbackListener() {
            @Override
            public void onSuccess(final String data) {
                Tasks.executeInBackground(context, new BackgroundWork<ActionResponse<String>>() {
                    @Override
                    public ActionResponse<String> doInBackground() throws Exception {
                        ApiResponse<String> result = GsonUtil.getInstance().fromJson(data, new TypeToken<ApiResponse<String>>() {
                        }.getType());
                        if(result.isLegal()){
                            user.setAutograph(result.getData());
                            DBManager.getInstance().updateAutograph(result.getData());
                            EventBus.getDefault().post(new UpdateAutographEvent(result.getData()));
                        }
                        return ActionResponse.getActionResponFromApiRespon(result);
                    }
                }, new Completion<ActionResponse<String>>() {
                    @Override
                    public void onSuccess(Context context, ActionResponse<String> result) {
                        actionStringCallbackListener.onSuccess(result);
                    }

                    @Override
                    public void onError(Context context, Exception e) {
                        actionStringCallbackListener.onSuccess(ActionResponse.<String>getActionResponError());
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
    public User getUserFromLocal(){
        if(user == null){
            //全局中只获取一次，不需要异步处理
            user = DBManager.getInstance().getUser(CustomSessionPreference.getInstance().getCustomSession().getUserNo());
        }
        return user;
    }

    @Override
    public void getUserInfo(final ActionStringCallbackListener<ActionResponse<User>> actionStringCallbackListener){
        User tmpUser = getUserFromLocal();
        if(tmpUser != null){
            actionStringCallbackListener.onSuccess(ActionResponse.getActionRespon(getUserFromLocal()));
        }
        //TODO 更改重新全量获取用户数据的时间间隔
        if(tmpUser == null || StringUtil.isEmpty(tmpUser.getUpdateTime()) || TimeUtil.getHourDifferenceBetweenTime(tmpUser.getUpdateTime(), TimeUtil.getCurrentTimeYYMMDD_HHMMSS()) > 1){
            ApiAction.getInstance().getUserInfo(new ApiStringCallbackListener() {
                @Override
                public void onSuccess(final String data) {
                    Tasks.executeInBackground(context, new BackgroundWork<ActionResponse<User>>() {
                        @Override
                        public ActionResponse<User> doInBackground() throws Exception {
                            ApiResponse<UserDto> result = GsonUtil.getInstance().fromJson(data, new TypeToken<ApiResponse<UserDto>>() {
                            }.getType());
                            if(result.isLegal()) {
                                DBManager.getInstance().saveUser(result.getData());
                                user = DBManager.getInstance().getUser(CustomSessionPreference.getInstance().getCustomSession().getUserNo());
                            }
                            return ActionResponse.getActionRespon(result.getMessage(), result.getRetCode(), getUserFromLocal());
                        }
                    }, new Completion<ActionResponse<User>>() {
                        @Override
                        public void onSuccess(Context context, ActionResponse<User> result) {
                            actionStringCallbackListener.onSuccess(result);
                        }

                        @Override
                        public void onError(Context context, Exception e) {
                            actionStringCallbackListener.onSuccess(ActionResponse.<User>getActionResponError());
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
}
