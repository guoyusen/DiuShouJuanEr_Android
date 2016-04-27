package com.bili.diushoujuaner.presenter.publisher;

import android.content.Context;
import android.util.Log;

import com.bili.diushoujuaner.model.actionhelper.action.FileAction;
import com.bili.diushoujuaner.model.actionhelper.action.RecallAction;
import com.bili.diushoujuaner.model.actionhelper.respon.ActionResponse;
import com.bili.diushoujuaner.model.apihelper.request.RecallPublishReq;
import com.bili.diushoujuaner.model.apihelper.request.RecallSerialReq;
import com.bili.diushoujuaner.utils.StringUtil;
import com.bili.diushoujuaner.utils.entity.dto.RecallDto;
import com.bili.diushoujuaner.model.callback.ActionFileCallbackListener;
import com.bili.diushoujuaner.model.callback.ActionStringCallbackListener;
import com.bili.diushoujuaner.model.tempHelper.RecallTemper;
import com.bili.diushoujuaner.model.eventhelper.PublishRecallEvent;
import com.bili.diushoujuaner.utils.ConstantUtil;
import com.bili.diushoujuaner.utils.entity.vo.ImageItemVo;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BiLi on 2016/4/12.
 */
public class RecallPublisher {

    public static final int STATUS_UPLOADING = 1;
    public static final int STATUS_NONE = 2;
    public static final int STATUS_ERROR = 3;

    private ArrayList<ImageItemVo> imageItemVoList;//当前发布的图片列表
    private List<OnPublishListener> publishListeners;//监听列表
    private static RecallPublisher recallPublisher;
    private Context context;
    private String content;//当前发布的文本
    private int status;//当前发布的状态
    private int currentPosition; //当前正在上传的图片索引
    private String serial;//单次发表过程中的序列号
    private boolean isErrorOccured = false;//上传过程中是否发生了错误

    public RecallPublisher(Context context){
        this.publishListeners = new ArrayList<>();
        this.imageItemVoList = new ArrayList<>();
        this.context = context;
        this.currentPosition = 0;
    }

    public static synchronized RecallPublisher getInstance(Context context){
        if(recallPublisher == null){
            recallPublisher = new RecallPublisher(context);
        }
        return recallPublisher;
    }

    public ArrayList<ImageItemVo> getImageItemVoList() {
        return imageItemVoList;
    }

    public void reset(){
        this.imageItemVoList.clear();
        this.status = STATUS_NONE;
        this.content = "";
        this.currentPosition = 0;
        this.serial = "";
        this.isErrorOccured = false;
    }

    public void register(OnPublishListener publishListener){
        this.publishListeners.add(publishListener);
    }

    public void unregister(OnPublishListener publishListener){
        this.publishListeners.remove(publishListener);
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public int getPublishStatus() {
        return status;
    }

    public boolean isErrorOccured() {
        return isErrorOccured;
    }

    public void publishRecall(final ArrayList<ImageItemVo> imageItemVoList, String content){
        reset();
        this.serial = StringUtil.getSerialNo();
        this.imageItemVoList.addAll(imageItemVoList);
        this.content = content;
        this.status = STATUS_UPLOADING;

        //通知正在发布中
        for(OnPublishListener publishListener : publishListeners){
            publishListener.onStartPublish();
        }
        if(this.imageItemVoList.size() <= 0){
            publishContent();
        }else{
            publishPicture();
        }
    }

    public void republishRecall(){
        this.serial = StringUtil.getSerialNo();
        this.status = STATUS_UPLOADING;
        this.currentPosition = 0;
        this.isErrorOccured = false;
        //通知正在发布中
        for(OnPublishListener publishListener : publishListeners){
            publishListener.onStartPublish();
        }
        if(this.imageItemVoList.size() <= 0){
            publishContent();
        }else{
            publishPicture();
        }
    }

    public void publishPicture(){
        for(final ImageItemVo item : this.imageItemVoList){
            if(isErrorOccured){
                return;
            }
            FileAction.getInstance(context).uploadRecallPic(new RecallSerialReq(this.serial),item.path, new ActionFileCallbackListener<ActionResponse<String>>() {
                @Override
                public void onSuccess(ActionResponse<String> result) {
                    //重新通知，保证数据正确
                    for(OnPublishListener publishListener : publishListeners){
                        publishListener.onProgress(currentPosition, 1.0f);
                    }

                    currentPosition ++;
                    if(currentPosition == imageItemVoList.size()){
                        publishContent();
                    }
                }

                @Override
                public void onFailure(int errorCode) {
                    status = STATUS_ERROR;
                    for(OnPublishListener publishListener : publishListeners){
                        publishListener.onError();
                    }
                    isErrorOccured = true;
                }

                @Override
                public void onProgress(float progress) {
                    for(OnPublishListener publishListener : publishListeners){
                        Log.d("guoyusenc","position = " + currentPosition + " progress = " + progress);
                        publishListener.onStartPublish();
                        publishListener.onProgress(currentPosition, progress);
                    }
                }
            });
        }

    }

    private void publishContent(){
        RecallAction.getInstance(context).getRecallPublish(new RecallPublishReq(content, imageItemVoList.size(),this.serial), new ActionStringCallbackListener<ActionResponse<RecallDto>>() {
            @Override
            public void onSuccess(ActionResponse<RecallDto> result) {
                if(result.getRetCode().equals(ConstantUtil.RETCODE_SUCCESS)){
                    for(OnPublishListener publishListener : publishListeners){
                        publishListener.onFinishPublish();
                    }
                    reset();
                    RecallTemper.getInstance().addRecallDtoToHomeList(result.getData());
                    RecallTemper.getInstance().addRecallDto(result.getData());
                    EventBus.getDefault().post(new PublishRecallEvent(result.getData()));
                }
            }
            @Override
            public void onFailure(int errorCode) {
                for(OnPublishListener publishListener : publishListeners){
                    publishListener.onError();
                }
            }
        });
    }

}
