package com.bili.diushoujuaner.model.apihelper.api;

import com.bili.diushoujuaner.model.apihelper.callback.ApiCallbackListener;
import com.bili.diushoujuaner.utils.Constant;
import com.bili.diushoujuaner.utils.request.RecallListReq;
import com.bili.diushoujuaner.utils.request.UserAccountReq;

/**
 * Created by BiLi on 2016/3/11.
 */
public interface Api {

    String getUserLogin = Constant.HOST_ADDRESS + "1.0/users/login";
    String getUserInfo = Constant.HOST_ADDRESS + "1.0/users/info";
    String getContactList = Constant.HOST_ADDRESS + "1.0/contacts";
    String getRecallList = Constant.HOST_ADDRESS + "1.0/recalls?";
    String getGoodAdd = Constant.HOST_ADDRESS + "1.0/goods/add";
    String getGoodRemove = Constant.HOST_ADDRESS + "1.0/goods/remove";

    /**
     * 用户登录
     * @param userAccountReq
     * @param apiCallbackListener
     */
    void getUserLogin(UserAccountReq userAccountReq, ApiCallbackListener apiCallbackListener);

    /**
     * 获取用户的个人信息
     * @param apiCallbackListener
     */
    void getUserInfo(ApiCallbackListener apiCallbackListener);

    /**
     * 获取用户的通讯录
     * @param apiCallbackListener
     */
    void getContactList(ApiCallbackListener apiCallbackListener);

    /**
     * 获取童趣列表
     * @param recallListReq
     * @param apiCallbackListener
     */
    void getRecallList(RecallListReq recallListReq, ApiCallbackListener apiCallbackListener);

    /**
     * 添加赞
     * @param recallNo
     * @param apiCallbackListener
     */
    void getGoodAdd(long recallNo, ApiCallbackListener apiCallbackListener);

    /**
     * 取消赞
     * @param recallNo
     * @param apiCallbackListener
     */
    void getGoodRemove(long recallNo, ApiCallbackListener apiCallbackListener);

}
