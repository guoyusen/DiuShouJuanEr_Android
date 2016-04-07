package com.bili.diushoujuaner.model.action.impl;

import android.content.Context;

import com.bili.diushoujuaner.model.action.IUserInfoAction;
import com.bili.diushoujuaner.model.action.respon.ActionRespon;
import com.bili.diushoujuaner.model.apihelper.ApiRespon;
import com.bili.diushoujuaner.model.apihelper.api.ApiAction;
import com.bili.diushoujuaner.model.apihelper.callback.ApiCallbackListener;
import com.bili.diushoujuaner.model.apihelper.request.AutographModifyReq;
import com.bili.diushoujuaner.model.apihelper.request.UserInfoReq;
import com.bili.diushoujuaner.model.callback.ActionCallbackListener;
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
    public void clearUser() {
        this.user = null;
    }

    @Override
    public void getUserInfoUpdate(UserInfoReq userInfoReq, final ActionCallbackListener<ActionRespon<User>> actionCallbackListener) {
        ApiAction.getInstance().getUserInfoUpdate(userInfoReq, new ApiCallbackListener() {
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
                        actionCallbackListener.onSuccess(result);
                    }

                    @Override
                    public void onError(Context context, Exception e) {
                        actionCallbackListener.onSuccess(ActionRespon.<User>getActionResponError());
                    }
                });
            }

            @Override
            public void onFailure(int errorCode) {
                actionCallbackListener.onFailure(errorCode);
            }
        });
    }

    @Override
    public void getAutographModify(AutographModifyReq autographModifyReq, final ActionCallbackListener<ActionRespon<String>> actionCallbackListener) {
        ApiAction.getInstance().getAutographModify(autographModifyReq, new ApiCallbackListener() {
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
                        actionCallbackListener.onSuccess(result);
                    }

                    @Override
                    public void onError(Context context, Exception e) {
                        actionCallbackListener.onSuccess(ActionRespon.<String>getActionResponError());
                    }
                });
            }

            @Override
            public void onFailure(int errorCode) {
                actionCallbackListener.onFailure(errorCode);
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
    public void getUserInfo(final ActionCallbackListener<ActionRespon<User>> actionCallbackListener){
        User tmpUser = getUserFromLocal();
        if(tmpUser != null){
            actionCallbackListener.onSuccess(ActionRespon.getActionRespon(getUserFromLocal()));
        }
        //TODO 更改重新全量获取用户数据的时间间隔
        if(tmpUser == null || Common.isEmpty(tmpUser.getUpdateTime()) || Common.getHourDifferenceBetweenTime(tmpUser.getUpdateTime(), Common.getCurrentTimeYYMMDD_HHMMSS()) > 1){
            ApiAction.getInstance().getUserInfo(new ApiCallbackListener() {
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
                            actionCallbackListener.onSuccess(result);
                        }

                        @Override
                        public void onError(Context context, Exception e) {
                            actionCallbackListener.onSuccess(ActionRespon.<User>getActionResponError());
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
}
