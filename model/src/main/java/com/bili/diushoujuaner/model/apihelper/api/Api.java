package com.bili.diushoujuaner.model.apihelper.api;

import com.bili.diushoujuaner.model.apihelper.callback.ApiCallbackListener;
import com.bili.diushoujuaner.model.apihelper.request.CommentAddReq;
import com.bili.diushoujuaner.model.apihelper.request.CommentRemoveReq;
import com.bili.diushoujuaner.model.apihelper.request.ContactInfoReq;
import com.bili.diushoujuaner.model.apihelper.request.RecentRecallReq;
import com.bili.diushoujuaner.model.apihelper.request.ResponAddReq;
import com.bili.diushoujuaner.model.apihelper.request.ResponRemoveReq;
import com.bili.diushoujuaner.utils.Constant;
import com.bili.diushoujuaner.model.apihelper.request.RecallListReq;
import com.bili.diushoujuaner.model.apihelper.request.UserAccountReq;

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
    String getCommentAdd = Constant.HOST_ADDRESS + "/1.0/comments/add";
    String getCommentRemove = Constant.HOST_ADDRESS + "1.0/comments/remove";
    String getResponAdd = Constant.HOST_ADDRESS + "1.0/respons/add";
    String getResponRemove = Constant.HOST_ADDRESS + "1.0/respons/remove";
    String getContactInfo = Constant.HOST_ADDRESS + "1.0/users/info/user?";
    String getRecentRecall = Constant.HOST_ADDRESS + "1.0/recalls/recent?";

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

    /**
     * 添加评论
     * @param commentAddReq
     * @param apiCallbackListener
     */
    void getCommentAdd(CommentAddReq commentAddReq, ApiCallbackListener apiCallbackListener);

    /**
     * 添加回复
     * @param responAddReq
     * @param apiCallbackListener
     */
    void getResponAdd(ResponAddReq responAddReq, ApiCallbackListener apiCallbackListener);

    /**
     * 删除评论
     * @param apiCallbackListener
     */
    void getCommentRemove(CommentRemoveReq commentRemoveReq, ApiCallbackListener apiCallbackListener);

    /**
     * 删除回复
     * @param apiCallbackListener
     */
    void getResponRemove(ResponRemoveReq responRemoveReq, ApiCallbackListener apiCallbackListener);

    /**
     * 获取用户的个人信息
     * @param contactInfoReq
     * @param apiCallbackListener
     */
    void getContactInfo(ContactInfoReq contactInfoReq, ApiCallbackListener apiCallbackListener);

    /**
     * 获取用户最新的Recall
     * @param recentRecallReq
     * @param apiCallbackListener
     */
    void getRecentRecall(RecentRecallReq recentRecallReq, ApiCallbackListener apiCallbackListener);

}
