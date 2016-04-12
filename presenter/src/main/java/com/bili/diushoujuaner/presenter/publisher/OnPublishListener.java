package com.bili.diushoujuaner.presenter.publisher;

/**
 * Created by BiLi on 2016/4/12.
 */
public interface OnPublishListener {

    void onProgress(int position, float progress);

    void onFinishPublish();

    void onStartPublish();

    void onError();

}
