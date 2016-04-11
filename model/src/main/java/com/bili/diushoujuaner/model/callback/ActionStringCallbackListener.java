package com.bili.diushoujuaner.model.callback;

/**
 * Created by BiLi on 2016/3/10.
 */
public interface ActionStringCallbackListener<T> {

    void onSuccess(T t);

    void onFailure(int errorCode);

}
