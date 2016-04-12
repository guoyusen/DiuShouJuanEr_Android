package com.bili.diushoujuaner.presenter.publisher;

import android.content.Context;
import android.util.Log;

import com.bili.diushoujuaner.model.action.impl.FileAction;
import com.bili.diushoujuaner.model.action.impl.RecallAction;
import com.bili.diushoujuaner.model.action.respon.ActionRespon;
import com.bili.diushoujuaner.model.apihelper.request.RecallPublishReq;
import com.bili.diushoujuaner.model.apihelper.response.RecallDto;
import com.bili.diushoujuaner.model.callback.ActionFileCallbackListener;
import com.bili.diushoujuaner.model.callback.ActionStringCallbackListener;
import com.bili.diushoujuaner.model.tempHelper.RecallTemper;
import com.bili.diushoujuaner.presenter.event.PublishRecallEvent;
import com.bili.diushoujuaner.utils.Constant;
import com.bili.diushoujuaner.utils.entity.ImageItemVo;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BiLi on 2016/4/12.
 */
public class RecallPublisher {

    public static final int STATUS_UPLOADING = 1;
    public static final int STATUS_NONE = 2;

    private ArrayList<ImageItemVo> imageItemVoList;
    private List<OnPublishListener> publishListeners;
    private static RecallPublisher recallPublisher;
    private String content;
    private int status;
    private Context context;
    private int currentPosition; //当前正在上传的图片索引

    public RecallPublisher(Context context){
        publishListeners = new ArrayList<>();
        imageItemVoList = new ArrayList<>();
        this.context = context;
        currentPosition = 0;
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
        imageItemVoList.clear();
        status = STATUS_NONE;
        content = "";
        currentPosition = 0;
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

    public void publishRecall(final ArrayList<ImageItemVo> imageItemVoList, String content){
        reset();
        this.imageItemVoList.addAll(imageItemVoList);
        this.content = content;
        status = STATUS_UPLOADING;

        //通知正在发布中
        for(OnPublishListener publishListener : publishListeners){
            publishListener.onStartPublish();
        }

        if(this.imageItemVoList.size() <= 0){
            publishContent();
        }else{
            for(final ImageItemVo item : this.imageItemVoList){
                FileAction.getInstance(context).uploadRecallPic(item.path, new ActionFileCallbackListener<ActionRespon<String>>() {
                    @Override
                    public void onSuccess(ActionRespon<String> result) {
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

    }

    private void publishContent(){
        RecallAction.getInstance(context).getRecallPublish(new RecallPublishReq(content, imageItemVoList.size()), new ActionStringCallbackListener<ActionRespon<RecallDto>>() {
            @Override
            public void onSuccess(ActionRespon<RecallDto> result) {
                if(result.getRetCode().equals(Constant.RETCODE_SUCCESS)){
                    for(OnPublishListener publishListener : publishListeners){
                        publishListener.onFinishPublish();
                    }
                    reset();
                    RecallTemper.addRecallDtoToHomeList(result.getData());
                    RecallTemper.addRecallDto(result.getData());
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
