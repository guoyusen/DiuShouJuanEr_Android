package com.bili.diushoujuaner.model.actionhelper.action;

import android.content.Context;

import com.bili.diushoujuaner.model.actionhelper.IFileAction;
import com.bili.diushoujuaner.model.actionhelper.respon.ActionResponse;
import com.bili.diushoujuaner.model.apihelper.ApiResponse;
import com.bili.diushoujuaner.model.apihelper.api.ApiAction;
import com.bili.diushoujuaner.model.apihelper.callback.ApiFileCallbackListener;
import com.bili.diushoujuaner.model.apihelper.request.PartyHeadUpdateReq;
import com.bili.diushoujuaner.model.apihelper.request.RecallSerialReq;
import com.bili.diushoujuaner.model.callback.ActionFileCallbackListener;
import com.bili.diushoujuaner.model.databasehelper.DBManager;
import com.bili.diushoujuaner.model.eventhelper.ShowHeadEvent;
import com.bili.diushoujuaner.model.eventhelper.UpdatePartyEvent;
import com.bili.diushoujuaner.model.eventhelper.UpdateWallPaperEvent;
import com.bili.diushoujuaner.model.tempHelper.ContactTemper;
import com.bili.diushoujuaner.utils.ConstantUtil;
import com.bili.diushoujuaner.utils.GsonUtil;
import com.google.gson.reflect.TypeToken;
import com.nanotasks.BackgroundWork;
import com.nanotasks.Completion;
import com.nanotasks.Tasks;

import org.greenrobot.eventbus.EventBus;

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
    public void upoadPartyHeadPic(final PartyHeadUpdateReq partyHeadUpdateReq, String path, final ActionFileCallbackListener<ActionResponse<String>> actionFileCallbackListener) {
        ApiAction.getInstance().getPartyHeadUpdate(partyHeadUpdateReq, path, new ApiFileCallbackListener() {
            @Override
            public void onSuccess(final String data) {
                Tasks.executeInBackground(context, new BackgroundWork<ActionResponse<String>>() {
                    @Override
                    public ActionResponse<String> doInBackground() throws Exception {
                        ApiResponse<String> result = GsonUtil.getInstance().fromJson(data, new TypeToken<ApiResponse<String>>() {
                        }.getType());
                        if(result.isLegal()){
                            ContactTemper.getInstance().updatePartyHeadPic(partyHeadUpdateReq.getPartyNo(), result.getData());
                            DBManager.getInstance().updatePartyHeadPic(partyHeadUpdateReq.getPartyNo(), result.getData());
                            EventBus.getDefault().post(new UpdatePartyEvent(partyHeadUpdateReq.getPartyNo(), result.getData(), ConstantUtil.CHAT_PARTY_HEAD));
                        }
                        return ActionResponse.getActionResponFromApiRespon(result);
                    }
                }, new Completion<ActionResponse<String>>() {
                    @Override
                    public void onSuccess(Context context, ActionResponse<String> result) {
                        actionFileCallbackListener.onSuccess(result);
                    }

                    @Override
                    public void onError(Context context, Exception e) {
                        actionFileCallbackListener.onSuccess(ActionResponse.<String>getActionResponError());
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
    public synchronized void uploadRecallPic(RecallSerialReq recallSerialReq, String path, final ActionFileCallbackListener<ActionResponse<String>> actionFileCallbackListener) {
        ApiAction.getInstance().getRecallPicUpload(recallSerialReq, path, new ApiFileCallbackListener() {
            @Override
            public void onSuccess(final String data) {
                Tasks.executeInBackground(context, new BackgroundWork<ActionResponse<String>>() {
                    @Override
                    public ActionResponse<String> doInBackground() throws Exception {
                        ApiResponse<String> result = GsonUtil.getInstance().fromJson(data, new TypeToken<ApiResponse<String>>() {
                        }.getType());
                        return ActionResponse.getActionResponFromApiRespon(result);
                    }
                }, new Completion<ActionResponse<String>>() {
                    @Override
                    public void onSuccess(Context context, ActionResponse<String> result) {
                        actionFileCallbackListener.onSuccess(result);
                    }

                    @Override
                    public void onError(Context context, Exception e) {
                        actionFileCallbackListener.onSuccess(ActionResponse.<String>getActionResponError());
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
    public void uploadWallpaper(String path, final ActionFileCallbackListener<ActionResponse<String>> actionFileCallbackListener) {
        ApiAction.getInstance().getWallpaperUpdate(path, new ApiFileCallbackListener() {
            @Override
            public void onSuccess(final String data) {
                Tasks.executeInBackground(context, new BackgroundWork<ActionResponse<String>>() {
                    @Override
                    public ActionResponse<String> doInBackground() throws Exception {
                        ApiResponse<String> result = GsonUtil.getInstance().fromJson(data, new TypeToken<ApiResponse<String>>() {
                        }.getType());
                        if(result.isLegal()){
                            DBManager.getInstance().updateWallpaper(result.getData());
                            //清空user，下次重新获取
                            UserInfoAction.getInstance(context).clearUser();
                            EventBus.getDefault().post(new UpdateWallPaperEvent(result.getData()));
                        }
                        return ActionResponse.getActionResponFromApiRespon(result);
                    }
                }, new Completion<ActionResponse<String>>() {
                    @Override
                    public void onSuccess(Context context, ActionResponse<String> result) {
                        actionFileCallbackListener.onSuccess(result);
                    }

                    @Override
                    public void onError(Context context, Exception e) {
                        actionFileCallbackListener.onSuccess(ActionResponse.<String>getActionResponError());
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
    public void uploadHeadPic(String path, final ActionFileCallbackListener<ActionResponse<String>> actionFileCallbackListener) {
        ApiAction.getInstance().getHeadUpdate(path, new ApiFileCallbackListener() {
            @Override
            public void onSuccess(final String data) {
                Tasks.executeInBackground(context, new BackgroundWork<ActionResponse<String>>() {
                    @Override
                    public ActionResponse<String> doInBackground() throws Exception {
                        ApiResponse<String> result = GsonUtil.getInstance().fromJson(data, new TypeToken<ApiResponse<String>>() {
                        }.getType());
                        if(result.isLegal()){
                            DBManager.getInstance().updateHeadPic(result.getData());
                            //clear user
                            UserInfoAction.getInstance(context).clearUser();
                            EventBus.getDefault().post(new ShowHeadEvent(result.getData()));
                        }
                        return ActionResponse.getActionResponFromApiRespon(result);
                    }
                }, new Completion<ActionResponse<String>>() {
                    @Override
                    public void onSuccess(Context context, ActionResponse<String> result) {
                        actionFileCallbackListener.onSuccess(result);
                    }

                    @Override
                    public void onError(Context context, Exception e) {
                        actionFileCallbackListener.onSuccess(ActionResponse.<String>getActionResponError());
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
