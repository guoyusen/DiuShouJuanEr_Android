package com.bili.diushoujuaner.model.apihelper.api;

import com.bili.diushoujuaner.model.apihelper.callback.ApiFileCallbackListener;
import com.bili.diushoujuaner.model.apihelper.callback.ApiStringCallbackListener;
import com.bili.diushoujuaner.model.apihelper.request.AcountUpdateReq;
import com.bili.diushoujuaner.model.apihelper.request.AutographModifyReq;
import com.bili.diushoujuaner.model.apihelper.request.CommentAddReq;
import com.bili.diushoujuaner.model.apihelper.request.CommentRemoveReq;
import com.bili.diushoujuaner.model.apihelper.request.ContactInfoReq;
import com.bili.diushoujuaner.model.apihelper.request.ContactsSearchReq;
import com.bili.diushoujuaner.model.apihelper.request.FeedBackReq;
import com.bili.diushoujuaner.model.apihelper.request.MemberNameUpdateReq;
import com.bili.diushoujuaner.model.apihelper.request.PartyHeadUpdateReq;
import com.bili.diushoujuaner.model.apihelper.request.PartyIntroduceUpdateReq;
import com.bili.diushoujuaner.model.apihelper.request.PartyNameUpdateReq;
import com.bili.diushoujuaner.model.apihelper.request.RecallPublishReq;
import com.bili.diushoujuaner.model.apihelper.request.RecallRemoveReq;
import com.bili.diushoujuaner.model.apihelper.request.RecallSerialReq;
import com.bili.diushoujuaner.model.apihelper.request.RecentRecallReq;
import com.bili.diushoujuaner.model.apihelper.request.ResponAddReq;
import com.bili.diushoujuaner.model.apihelper.request.ResponRemoveReq;
import com.bili.diushoujuaner.model.apihelper.request.UserInfoReq;
import com.bili.diushoujuaner.model.apihelper.request.VerifyReq;
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
    String getCommentAdd = Constant.HOST_ADDRESS + "1.0/comments/add";
    String getCommentRemove = Constant.HOST_ADDRESS + "1.0/comments/remove";
    String getResponAdd = Constant.HOST_ADDRESS + "1.0/respons/add";
    String getResponRemove = Constant.HOST_ADDRESS + "1.0/respons/remove";
    String getContactInfo = Constant.HOST_ADDRESS + "1.0/users/info/user?";
    String getRecentRecall = Constant.HOST_ADDRESS + "1.0/recalls/recent?";
    String getAutographModify = Constant.HOST_ADDRESS + "1.0/users/modify/autograph";
    String getFeedBackAdd = Constant.HOST_ADDRESS + "1.0/feedback/add";
    String getUserInfoUpdate = Constant.HOST_ADDRESS + "1.0/users/info/update";
    String getVerifyCode = Constant.HOST_ADDRESS + "1.0/verify/code";
    String getAcountRegist = Constant.HOST_ADDRESS + "1.0/users/regist";
    String getAcountReset = Constant.HOST_ADDRESS + "1.0/users/reset";
    String getLogout = Constant.HOST_ADDRESS + "1.0/users/logout";
    String getRecallRemove = Constant.HOST_ADDRESS + "1.0/recalls/remove";
    String getHeadUpdate = Constant.HOST_ADDRESS + "1.0/file/headpic";
    String getWallpaperUpdate = Constant.HOST_ADDRESS + "1.0/file/wallpaper";
    String getRecallPicUpload = Constant.HOST_ADDRESS + "1.0/file/recallpic";
    String getRecallPublish = Constant.HOST_ADDRESS + "1.0/recalls/add";
    String getOffMsg = Constant.HOST_ADDRESS + "1.0/offmsgs";
    String getMemberNameUpdate = Constant.HOST_ADDRESS + "1.0/member/modify/name";
    String getPartyHeadUpdate = Constant.HOST_ADDRESS + "1.0/file/party/headpic";
    String getPartyNameUpdate = Constant.HOST_ADDRESS + "1.0/party/modify/name";
    String getPartyIntroduceUpdate = Constant.HOST_ADDRESS + "1.0/party/modify/introduce";
    String getContactsSearch = Constant.HOST_ADDRESS + "1.0/contacts/search";

    /**
     * 用户登录
     * @param userAccountReq
     * @param apiStringCallbackListener
     */
    void getUserLogin(UserAccountReq userAccountReq, ApiStringCallbackListener apiStringCallbackListener);

    /**
     * 获取用户的个人信息
     * @param apiStringCallbackListener
     */
    void getUserInfo(ApiStringCallbackListener apiStringCallbackListener);

    /**
     * 获取用户的通讯录
     * @param apiStringCallbackListener
     */
    void getContactList(ApiStringCallbackListener apiStringCallbackListener);

    /**
     * 获取童趣列表
     * @param recallListReq
     * @param apiStringCallbackListener
     */
    void getRecallList(RecallListReq recallListReq, ApiStringCallbackListener apiStringCallbackListener);

    /**
     * 添加赞
     * @param recallNo
     * @param apiStringCallbackListener
     */
    void getGoodAdd(long recallNo, ApiStringCallbackListener apiStringCallbackListener);

    /**
     * 取消赞
     * @param recallNo
     * @param apiStringCallbackListener
     */
    void getGoodRemove(long recallNo, ApiStringCallbackListener apiStringCallbackListener);

    /**
     * 添加评论
     * @param commentAddReq
     * @param apiStringCallbackListener
     */
    void getCommentAdd(CommentAddReq commentAddReq, ApiStringCallbackListener apiStringCallbackListener);

    /**
     * 添加回复
     * @param responAddReq
     * @param apiStringCallbackListener
     */
    void getResponAdd(ResponAddReq responAddReq, ApiStringCallbackListener apiStringCallbackListener);

    /**
     * 删除评论
     * @param apiStringCallbackListener
     */
    void getCommentRemove(CommentRemoveReq commentRemoveReq, ApiStringCallbackListener apiStringCallbackListener);

    /**
     * 删除回复
     * @param apiStringCallbackListener
     */
    void getResponRemove(ResponRemoveReq responRemoveReq, ApiStringCallbackListener apiStringCallbackListener);

    /**
     * 获取用户的个人信息
     * @param contactInfoReq
     * @param apiStringCallbackListener
     */
    void getContactInfo(ContactInfoReq contactInfoReq, ApiStringCallbackListener apiStringCallbackListener);

    /**
     * 获取用户最新的Recall
     * @param recentRecallReq
     * @param apiStringCallbackListener
     */
    void getRecentRecall(RecentRecallReq recentRecallReq, ApiStringCallbackListener apiStringCallbackListener);

    /**
     * 更新用户签名
     * @param autographModifyReq
     * @param apiStringCallbackListener
     */
    void getAutographModify(AutographModifyReq autographModifyReq, ApiStringCallbackListener apiStringCallbackListener);

    /**
     * 提交用户意见反馈
     * @param feedBackReq
     * @param apiStringCallbackListener
     */
    void getFeedBackAdd(FeedBackReq feedBackReq, ApiStringCallbackListener apiStringCallbackListener);

    /**
     * 更新用户资料
     * @param userInfoReq
     * @param apiStringCallbackListener
     */
    void getUserInfoUpdate(UserInfoReq userInfoReq, ApiStringCallbackListener apiStringCallbackListener);

    /**
     * 获取验证码
     * @param verifyReq
     * @param apiStringCallbackListener
     */
    void getVerifyCode(VerifyReq verifyReq, ApiStringCallbackListener apiStringCallbackListener);

    /**
     * 账户注册
     * @param acountUpdateReq
     * @param apiStringCallbackListener
     */
    void getAcountRegist(AcountUpdateReq acountUpdateReq, ApiStringCallbackListener apiStringCallbackListener);

    /**
     * 账户重置
     * @param acountUpdateReq
     * @param apiStringCallbackListener
     */
    void getAcountReset(AcountUpdateReq acountUpdateReq, ApiStringCallbackListener apiStringCallbackListener);

    /**
     * 账户退出
     * @param apiStringCallbackListener
     */
    void getLogout(ApiStringCallbackListener apiStringCallbackListener);

    /**
     * 删除Recall
     * @param recallRemoveReq
     * @param apiStringCallbackListener
     */
    void getRecallRemove(RecallRemoveReq recallRemoveReq, ApiStringCallbackListener apiStringCallbackListener);

    /**
     * 上传头像
     * @param path
     * @param apiFileCallbackListener
     */
    void getHeadUpdate(String path, ApiFileCallbackListener apiFileCallbackListener);

    /**
     * 上传壁纸
     * @param path
     * @param apiFileCallbackListener
     */
    void getWallpaperUpdate(String path, ApiFileCallbackListener apiFileCallbackListener);

    /**
     * 上传recallPic
     * @param path
     * @param apiFileCallbackListener
     */
    void getRecallPicUpload(RecallSerialReq recallSerialReq, String path, ApiFileCallbackListener apiFileCallbackListener);

    /**
     * 发布Recall
     * @param recallPublishReq
     * @param apiStringCallbackListener
     */
    void getRecallPublish(RecallPublishReq recallPublishReq, ApiStringCallbackListener apiStringCallbackListener);

    /**
     * 获取离线信息
     * @param apiStringCallbackListener
     */
    void getOffMsg(ApiStringCallbackListener apiStringCallbackListener);

    /**
     * 更新群名片
     * @param memberNameUpdateReq
     * @param apiStringCallbackListener
     */
    void getMemberNameUpdate(MemberNameUpdateReq memberNameUpdateReq, ApiStringCallbackListener apiStringCallbackListener);

    /**
     * 上传群头像
     * @param path
     * @param apiFileCallbackListener
     */
    void getPartyHeadUpdate(PartyHeadUpdateReq partyHeadUpdateReq, String path, ApiFileCallbackListener apiFileCallbackListener);

    /**
     * 更新群名称
     * @param partyNameUpdateReq
     * @param apiStringCallbackListener
     */
    void getPartyNameUpdate(PartyNameUpdateReq partyNameUpdateReq, ApiStringCallbackListener apiStringCallbackListener);

    /**
     * 更新群介绍
     * @param partyIntroduceUpdateReq
     * @param apiStringCallbackListener
     */
    void getPartyIntroduceUpdate(PartyIntroduceUpdateReq partyIntroduceUpdateReq, ApiStringCallbackListener apiStringCallbackListener);

    /**
     * 搜索童友
     * @param contactsSearchReq
     * @param apiStringCallbackListener
     */
    void getContactsSearch(ContactsSearchReq contactsSearchReq, ApiStringCallbackListener apiStringCallbackListener);
}
