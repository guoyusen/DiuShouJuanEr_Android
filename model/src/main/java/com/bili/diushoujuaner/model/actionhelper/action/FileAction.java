package com.bili.diushoujuaner.model.actionhelper.action;

import android.content.Context;

import com.bili.diushoujuaner.model.actionhelper.IFileAction;
import com.bili.diushoujuaner.model.actionhelper.respon.ActionRespon;
import com.bili.diushoujuaner.model.apihelper.ApiRespon;
import com.bili.diushoujuaner.model.apihelper.api.ApiAction;
import com.bili.diushoujuaner.model.apihelper.callback.ApiFileCallbackListener;
import com.bili.diushoujuaner.model.apihelper.request.PartyHeadUpdateReq;
import com.bili.diushoujuaner.model.apihelper.request.RecallSerialReq;
import com.bili.diushoujuaner.model.callback.ActionFileCallbackListener;
import com.bili.diushoujuaner.model.databasehelper.DBManager;
import com.bili.diushoujuaner.model.tempHelper.ContactTemper;
import com.bili.diushoujuaner.utils.GsonUtil;
import com.google.gson.reflect.TypeToken;
import com.nanotasks.BackgroundWork;
import com.nanotasks.Completion;
import com.nanotasks.Tasks;

/**
 * Created by BiLi on 2016/4/11.
 */
public class FileAction implements IFileAction {

    private static FileAction fileAction;
    private Context context;

    public FileAction(Context context) {
        this.context = context;
    }

    public static synchronized FileAction getInstance(Context context){
        if(fileAction == null){
            fileAction = new FileAction(context);
        }
        return fileAction;
    }

    @Override
    public void upoadPartyHeadPic(final PartyHeadUpdateReq partyHeadUpdateReq, String path, final ActionFileCallbackListener<ActionRespon<String>> actionFileCallbackListener) {
        ApiAction.getInstance().getPartyHeadUpdate(partyHeadUpdateReq, path, new ApiFileCallbackListener() {
            @Override
            public void onSuccess(final String data) {
                Tasks.executeInBackground(context, new BackgroundWork<ActionRespon<String>>() {
                    @Override
                    public ActionRespon<String> doInBackground() throws Exception {
                        ApiRespon<String> result = GsonUtil.getInstance().fromJson(data, new TypeToken<ApiRespon<String>>() {
                        }.getType());
                        if(result.isLegal()){
                            ContactTemper.getInstance().updatePartyHeadPic(partyHeadUpdateReq.getPartyNo(), result.getData());
                            DBManager.getInstance().updatePartyHeadPic(partyHeadUpdateReq.getPartyNo(), result.getData());
                        }
                        return ActionRespon.getActionResponFromApiRespon(result);
                    }
                }, new Completion<ActionRespon<String>>() {
                    @Override
                    public void onSuccess(Context context, ActionRespon<String> result) {
                        actionFileCallbackListener.onSuccess(result);
                    }

                    @Override
                    public void onError(Context context, Exception e) {
                        actionFileCallbackListener.onSuccess(ActionRespon.<String>getActionResponError());
                    }
                });
            }

            @Override
            public void onFailure(int errorCode) {
                actionFileCallbackListener.onFailure(errorCode);
            }

            @Override
            public void onProgress(float progress) {
                actionFileCallbackListener.onProgress(progress);
            }
        });
    }

    @Override
    public synchronized void uploadRecallPic(RecallSerialReq recallSerialReq, String path, final ActionFileCallbackListener<ActionRespon<String>> actionFileCallbackListener) {
        ApiAction.getInstance().getRecallPicUpload(recallSerialReq, path, new ApiFileCallbackListener() {
            @Override
            public void onSuccess(final String data) {
                Tasks.executeInBackground(context, new BackgroundWork<ActionRespon<String>>() {
                    @Override
                    public ActionRespon<String> doInBackground() throws Exception {
                        ApiRespon<String> result = GsonUtil.getInstance().fromJson(data, new TypeToken<ApiRespon<String>>() {
                        }.getType());
                        return ActionRespon.getActionResponFromApiRespon(result);
                    }
                }, new Completion<ActionRespon<String>>() {
                    @Override
                    public void onSuccess(Context context, ActionRespon<String> result) {
                        actionFileCallbackListener.onSuccess(result);
                    }

                    @Override
                    public void onError(Context context, Exception e) {
                        actionFileCallbackListener.onSuccess(ActionRespon.<String>getActionResponError());
                    }
                });
            }

            @Override
            public void onFailure(int errorCode) {
                actionFileCallbackListener.onFailure(errorCode);
            }

            @Override
            public void onProgress(float progress) {
                actionFileCallbackListener.onProgress(progress);
            }
        });
    }

    @Override
    public void uploadWallpaper(String path, final ActionFileCallbackListener<ActionRespon<String>> actionFileCallbackListener) {
        ApiAction.getInstance().getWallpaperUpdate(path, new ApiFileCallbackListener() {
            @Override
            public void onSuccess(final String data) {
                Tasks.executeInBackground(context, new BackgroundWork<ActionRespon<String>>() {
                    @Override
                    public ActionRespon<String> doInBackground() throws Exception {
                        ApiRespon<String> result = GsonUtil.getInstance().fromJson(data, new TypeToken<ApiRespon<String>>() {
                        }.getType());
                        if(result.isLegal()){
                            DBManager.getInstance().updateWallpaper(result.getData());
                            //clear user
                            UserInfoAction.getInstance(context).clearUser();
                        }
                        return ActionRespon.getActionResponFromApiRespon(result);
                    }
                }, new Completion<ActionRespon<String>>() {
                    @Override
                    public void onSuccess(Context context, ActionRespon<String> result) {
                        actionFileCallbackListener.onSuccess(result);
                    }

                    @Override
                    public void onError(Context context, Exception e) {
                        actionFileCallbackListener.onSuccess(ActionRespon.<String>getActionResponError());
                    }
                });
            }

            @Override
            public void onFailure(int errorCode) {
                actionFileCallbackListener.onFailure(errorCode);
            }

            @Override
            public void onProgress(float progress) {
                actionFileCallbackListener.onProgress(progress);
            }
        });
    }

    @Override
    public void uploadHeadPic(String path, final ActionFileCallbackListener<ActionRespon<String>> actionFileCallbackListener) {
        ApiAction.getInstance().getHeadUpdate(path, new ApiFileCallbackListener() {
            @Override
            public void onSuccess(final String data) {
                Tasks.executeInBackground(context, new BackgroundWork<ActionRespon<String>>() {
                    @Override
                    public ActionRespon<String> doInBackground() throws Exception {
                        ApiRespon<String> result = GsonUtil.getInstance().fromJson(data, new TypeToken<ApiRespon<String>>() {
                        }.getType());
                        if(result.isLegal()){
                            DBManager.getInstance().updateHeadPic(result.getData());
                            //clear user
                            UserInfoAction.getInstance(context).clearUser();
                        }
                        return ActionRespon.getActionResponFromApiRespon(result);
                    }
                }, new Completion<ActionRespon<String>>() {
                    @Override
                    public void onSuccess(Context context, ActionRespon<String> result) {
                        actionFileCallbackListener.onSuccess(result);
                    }

                    @Override
                    public void onError(Context context, Exception e) {
                        actionFileCallbackListener.onSuccess(ActionRespon.<String>getActionResponError());
                    }
                });
            }

            @Override
            public void onFailure(int errorCode) {
                actionFileCallbackListener.onFailure(errorCode);
            }

            @Override
            public void onProgress(float progress) {
                actionFileCallbackListener.onProgress(progress);
            }
        });
    }
}
