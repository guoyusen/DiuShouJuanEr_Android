package com.bili.diushoujuaner.model.api.callback;

/**
 * Created by BiLi on 2016/3/11.
 */
public interface ApiCallbackListener {

    /**
     * 成功时调用
     *
     * @param data 返回的数据
     */
    public void onSuccess(String data);

    /**
     * 失败时调用
     *
     * @param errorCode 错误码
     */
    public void onFailure(int errorCode);

}
