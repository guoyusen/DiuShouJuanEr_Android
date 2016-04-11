package com.bili.diushoujuaner.model.action.impl;

import android.content.Context;

import com.bili.diushoujuaner.model.action.IUserInfoAction;
import com.bili.diushoujuaner.model.action.respon.ActionRespon;
import com.bili.diushoujuaner.model.apihelper.ApiRespon;
import com.bili.diushoujuaner.model.apihelper.api.ApiAction;
import com.bili.diushoujuaner.model.apihelper.callback.ApiStringCallbackListener;
import com.bili.diushoujuaner.model.apihelper.request.AcountUpdateReq;
import com.bili.diushoujuaner.model.apihelper.request.AutographModifyReq;
import com.bili.diushoujuaner.model.apihelper.request.UserInfoReq;
import com.bili.diushoujuaner.model.apihelper.request.VerifyReq;
import com.bili.diushoujuaner.model.apihelper.response.CustomSession;
import com.bili.diushoujuaner.model.callback.ActionStringCallbackListener;
import com.bili.diushoujuaner.model.databasehelper.DBManager;
import com.bili.diushoujuaner.model.databasehelper.dao.User;
import com.bili.diushoujuaner.model.preferhelper.CustomSessionPreference;
import com.bili.diushoujuaner.utils.Common;
import com.bili.diushoujuaner.utils.GsonParser;
import com.bili.diushoujuaner.model.apihelper.response.UserRes;
import com.google.gson.reflect.TypeToken;
import com.nanotasks.BackgroundWork;
import com.nanotasks.Completion;
import com.nanotasks.Tasks;

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
    public void getLogout(final ActionStringCallbackListener<ActionRespon<Void>> actionStringCallbackListener) {
        ApiAction.getInstance().getLogout(new ApiStringCallbackListener() {
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

    @Override
    public void getAcountReset(AcountUpdateReq acountUpdateReq, final ActionStringCallbackListener<ActionRespon<Void>> actionStringCallbackListener) {
        ApiAction.getInstance().getAcountReset(acountUpdateReq, new ApiStringCallbackListener() {
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
    public void getAcountRegist(AcountUpdateReq acountUpdateReq, final ActionStringCallbackListener<ActionRespon<Void>> actionStringCallbackListener) {
        ApiAction.getInstance().getAcountRegist(acountUpdateReq, new ApiStringCallbackListener() {
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

    private void processCustomSessionData(final String data, final ActionStringCallbackListener<ActionRespon<Void>> actionStringCallbackListener){
        Tasks.executeInBackground(context, new BackgroundWork<ActionRespon<Void>>() {
            @Override
            public ActionRespon<Void> doInBackground() throws Exception {
                ApiRespon<CustomSession> result = GsonParser.getInstance().fromJson(data, new TypeToken<ApiRespon<CustomSession>>() {
                }.getType());
                if(result.getIsLegal()){
                    CustomSessionPreference.getInstance().saveCustomSession(result.getData());
                }
                return ActionRespon.getActionRespon(result.getMessage(), result.getRetCode(), (Void)null);
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
    public void getVerifyCode(VerifyReq verifyReq, final ActionStringCallbackListener<ActionRespon<Void>> actionStringCallbackListener) {
        ApiAction.getInstance().getVerifyCode(verifyReq, new ApiStringCallbackListener() {
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

    @Override
    public void clearUser() {
        this.user = null;
    }

    @Override
    public void getUserInfoUpdate(UserInfoReq userInfoReq, final ActionStringCallbackListener<ActionRespon<User>> actionStringCallbackListener) {
        ApiAction.getInstance().getUserInfoUpdate(userInfoReq, new ApiStringCallbackListener() {
            @Override
            public void onSuccess(final String data) {
                Tasks.executeInBackground(context, new BackgroundWork<ActionRespon<User>>() {
                    @Override
                    public ActionRespon<User> doInBackground() throws Exception {
                        ApiRespon<UserRes> result = GsonParser.getInstance().fromJson(data, new TypeToken<ApiRespon<UserRes>>() {
                        }.getType());
                        if(result.getIsLegal()) {
                            DBManager.getInstance().saveUser(result.getData());
                            user = DBManager.getInstance().getUser(CustomSessionPreference.getInstance().getCustomSession().getUserNo());
                        }
                        return ActionRespon.getActionRespon(result.getMessage(), result.getRetCode(), getUserFromLocal());
                    }
                }, new Completion<ActionRespon<User>>() {
                    @Override
                    public void onSuccess(Context context, ActionRespon<User> result) {
                        actionStringCallbackListener.onSuccess(result);
                    }

                    @Override
                    public void onError(Context context, Exception e) {
                        actionStringCallbackListener.onSuccess(ActionRespon.<User>getActionResponError());
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
    public void getAutographModify(AutographModifyReq autographModifyReq, final ActionStringCallbackListener<ActionRespon<String>> actionStringCallbackListener) {
        ApiAction.getInstance().getAutographModify(autographModifyReq, new ApiStringCallbackListener() {
            @Override
            public void onSuccess(final String data) {
                Tasks.executeInBackground(context, new BackgroundWork<ActionRespon<String>>() {
                    @Override
                    public ActionRespon<String> doInBackground() throws Exception {
                        ApiRespon<String> result = GsonParser.getInstance().fromJson(data, new TypeToken<ApiRespon<String>>() {
                        }.getType());
                        return ActionRespon.getActionResponFromApiRespon(result);
                    }
                }, new Completion<ActionRespon<String>>() {
                    @Override
                    public void onSuccess(Context context, ActionRespon<String> result) {
                        user.setAutograph(result.getData());
                        DBManager.getInstance().updateAutograph(result.getData());
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
    public User getUserFromLocal(){
        if(user == null){
            //全局中只获取一次，不需要异步处理
            user = DBManager.getInstance().getUser(CustomSessionPreference.getInstance().getCustomSession().getUserNo());
        }
        return user;
    }

    @Override
    public void getUserInfo(final ActionStringCallbackListener<ActionRespon<User>> actionStringCallbackListener){
        User tmpUser = getUserFromLocal();
        if(tmpUser != null){
            actionStringCallbackListener.onSuccess(ActionRespon.getActionRespon(getUserFromLocal()));
        }
        //TODO 更改重新全量获取用户数据的时间间隔
        if(tmpUser == null || Common.isEmpty(tmpUser.getUpdateTime()) || Common.getHourDifferenceBetweenTime(tmpUser.getUpdateTime(), Common.getCurrentTimeYYMMDD_HHMMSS()) > 1){
            ApiAction.getInstance().getUserInfo(new ApiStringCallbackListener() {
                @Override
                public void onSuccess(final String data) {
                    Tasks.executeInBackground(context, new BackgroundWork<ActionRespon<User>>() {
                        @Override
                        public ActionRespon<User> doInBackground() throws Exception {
                            ApiRespon<UserRes> result = GsonParser.getInstance().fromJson(data, new TypeToken<ApiRespon<UserRes>>() {
                            }.getType());
                            if(result.getIsLegal()) {
                                DBManager.getInstance().saveUser(result.getData());
                                user = DBManager.getInstance().getUser(CustomSessionPreference.getInstance().getCustomSession().getUserNo());
                            }
                            return ActionRespon.getActionRespon(result.getMessage(), result.getRetCode(), getUserFromLocal());
                        }
                    }, new Completion<ActionRespon<User>>() {
                        @Override
                        public void onSuccess(Context context, ActionRespon<User> result) {
                            actionStringCallbackListener.onSuccess(result);
                        }

                        @Override
                        public void onError(Context context, Exception e) {
                            actionStringCallbackListener.onSuccess(ActionRespon.<User>getActionResponError());
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
