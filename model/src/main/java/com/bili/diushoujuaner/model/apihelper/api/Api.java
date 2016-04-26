package com.bili.diushoujuaner.model.apihelper.api;

import com.bili.diushoujuaner.model.apihelper.callback.ApiFileCallbackListener;
import com.bili.diushoujuaner.model.apihelper.callback.ApiStringCallbackListener;
import com.bili.diushoujuaner.model.apihelper.request.AcountUpdateReq;
import com.bili.diushoujuaner.model.apihelper.request.AutographModifyReq;
import com.bili.diushoujuaner.model.apihelper.request.CommentAddReq;
import com.bili.diushoujuaner.model.apihelper.request.CommentRemoveReq;
import com.bili.diushoujuaner.model.apihelper.request.ContactAddInfoReq;
import com.bili.diushoujuaner.model.apihelper.request.ContactsSearchReq;
import com.bili.diushoujuaner.model.apihelper.request.FeedBackReq;
import com.bili.diushoujuaner.model.apihelper.request.FriendApplyReq;
import com.bili.diushoujuaner.model.apihelper.request.FriendAgreeReq;
import com.bili.diushoujuaner.model.apihelper.request.FriendDeleteReq;
import com.bili.diushoujuaner.model.apihelper.request.MemberExitReq;
import com.bili.diushoujuaner.model.apihelper.request.MemberNameUpdateReq;
import com.bili.diushoujuaner.model.apihelper.request.PartyAddReq;
import com.bili.diushoujuaner.model.apihelper.request.PartyApplyAgreeReq;
import com.bili.diushoujuaner.model.apihelper.request.PartyApplyReq;
import com.bili.diushoujuaner.model.apihelper.request.PartyContactReq;
import com.bili.diushoujuaner.model.apihelper.request.PartyHeadUpdateReq;
import com.bili.diushoujuaner.model.apihelper.request.PartyIntroduceUpdateReq;
import com.bili.diushoujuaner.model.apihelper.request.PartyNameUpdateReq;
import com.bili.diushoujuaner.model.apihelper.request.RecallPublishReq;
import com.bili.diushoujuaner.model.apihelper.request.RecallRemoveReq;
import com.bili.diushoujuaner.model.apihelper.request.RecallSerialReq;
import com.bili.diushoujuaner.model.apihelper.request.RecentRecallReq;
import com.bili.diushoujuaner.model.apihelper.request.RemarkUpdateReq;
import com.bili.diushoujuaner.model.apihelper.request.ResponAddReq;
import com.bili.diushoujuaner.model.apihelper.request.ResponRemoveReq;
import com.bili.diushoujuaner.model.apihelper.request.UserInfoReq;
import com.bili.diushoujuaner.model.apihelper.request.VerifyReq;
import com.bili.diushoujuaner.utils.ConstantUtil;
import com.bili.diushoujuaner.model.apihelper.request.RecallListReq;
import com.bili.diushoujuaner.model.apihelper.request.UserAccountReq;

/**
 * Created by BiLi on 2016/3/11.
 */
public interface Api {

    String getUserLogin = ConstantUtil.HOST_ADDRESS + "1.0/users/login";
    String getUserInfo = ConstantUtil.HOST_ADDRESS + "1.0/users/info";
    String getContactList = ConstantUtil.HOST_ADDRESS + "1.0/contacts";
    String getRecallList = ConstantUtil.HOST_ADDRESS + "1.0/recalls?";
    String getGoodAdd = ConstantUtil.HOST_ADDRESS + "1.0/goods/add";
    String getGoodRemove = ConstantUtil.HOST_ADDRESS + "1.0/goods/remove";
    String getCommentAdd = ConstantUtil.HOST_ADDRESS + "1.0/comments/add";
    String getCommentRemove = ConstantUtil.HOST_ADDRESS + "1.0/comments/remove";
    String getResponAdd = ConstantUtil.HOST_ADDRESS + "1.0/respons/add";
    String getResponRemove = ConstantUtil.HOST_ADDRESS + "1.0/respons/remove";
    String getContactInfo = ConstantUtil.HOST_ADDRESS + "1.0/users/info/user?";
    String getRecentRecall = ConstantUtil.HOST_ADDRESS + "1.0/recalls/recent?";
    String getAutographModify = ConstantUtil.HOST_ADDRESS + "1.0/users/modify/autograph";
    String getFeedBackAdd = ConstantUtil.HOST_ADDRESS + "1.0/feedback/add";
    String getUserInfoUpdate = ConstantUtil.HOST_ADDRESS + "1.0/users/info/update";
    String getVerifyCode = ConstantUtil.HOST_ADDRESS + "1.0/verify/code";
    String getAcountRegist = ConstantUtil.HOST_ADDRESS + "1.0/users/regist";
    String getAcountReset = ConstantUtil.HOST_ADDRESS + "1.0/users/reset";
    String getLogout = ConstantUtil.HOST_ADDRESS + "1.0/users/logout";
    String getRecallRemove = ConstantUtil.HOST_ADDRESS + "1.0/recalls/remove";
    String getHeadUpdate = ConstantUtil.HOST_ADDRESS + "1.0/file/headpic";
    String getWallpaperUpdate = ConstantUtil.HOST_ADDRESS + "1.0/file/wallpaper";
    String getRecallPicUpload = ConstantUtil.HOST_ADDRESS + "1.0/file/recallpic";
    String getRecallPublish = ConstantUtil.HOST_ADDRESS + "1.0/recalls/add";
    String getOffMsg = ConstantUtil.HOST_ADDRESS + "1.0/offmsgs";
    String getMemberNameUpdate = ConstantUtil.HOST_ADDRESS + "1.0/member/modify/name";
    String getPartyHeadUpdate = ConstantUtil.HOST_ADDRESS + "1.0/file/party/headpic";
    String getPartyNameUpdate = ConstantUtil.HOST_ADDRESS + "1.0/party/modify/name";
    String getPartyIntroduceUpdate = ConstantUtil.HOST_ADDRESS + "1.0/party/modify/introduce";
    String getContactsSearch = ConstantUtil.HOST_ADDRESS + "1.0/contacts/search?";
    String getFriendApply = ConstantUtil.HOST_ADDRESS + "1.0/friend/apply";
    String getFriendAgree = ConstantUtil.HOST_ADDRESS + "1.0/friend/agree";
    String getFriendDelete = ConstantUtil.HOST_ADDRESS + "1.0/friend/delete";
    String getFriendRemarkUpdate = ConstantUtil.HOST_ADDRESS + "1.0/friend/remark/modify";
    String getPartyAdd = ConstantUtil.HOST_ADDRESS + "1.0/party/add";
    String getPartyApply = ConstantUtil.HOST_ADDRESS + "1.0/party/apply";
    String getContactParty = ConstantUtil.HOST_ADDRESS + "1.0/contact/party?";
    String getPartyApplyAgree = ConstantUtil.HOST_ADDRESS + "1.0/party/apply/agree";
    String getMemberExit = ConstantUtil.HOST_ADDRESS + "1.0/party/exit";

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
     * @param contactAddInfoReq
     * @param apiStringCallbackListener
     */
    void getContactInfo(ContactAddInfoReq contactAddInfoReq, ApiStringCallbackListener apiStringCallbackListener);

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

    /**
     * 添加好友
     * @param friendApplyReq
     * @param apiStringCallbackListener
     */
    void getFriendApply(FriendApplyReq friendApplyReq, ApiStringCallbackListener apiStringCallbackListener);

    /**
     * 同意好友添加请求
     * @param friendAgreeReq
     * @param apiStringCallbackListener
     */
    void getFriendAgree(FriendAgreeReq friendAgreeReq,  ApiStringCallbackListener apiStringCallbackListener);

    /**
     * 删除好友关系
     * @param friendDeleteReq
     * @param apiStringCallbackListener
     */
    void getFriendDelete(FriendDeleteReq friendDeleteReq, ApiStringCallbackListener apiStringCallbackListener);

    /**
     * 修改备注名
     * @param remarkUpdateReq
     * @param apiStringCallbackListener
     */
    void getfriendRemarkUpdate(RemarkUpdateReq remarkUpdateReq, ApiStringCallbackListener apiStringCallbackListener);

    /**
     * 创建群
     * @param partyAddReq
     * @param path
     * @param apiFileCallbackListener
     */
    void getPartyAdd(PartyAddReq partyAddReq, String path, ApiFileCallbackListener apiFileCallbackListener);

    /**
     * 申请加群
     * @param partyApplyReq
     * @param apiStringCallbackListener
     */
    void getPartyApply(PartyApplyReq partyApplyReq, ApiStringCallbackListener apiStringCallbackListener);

    /**
     * 获取群的所有信息
     * @param partyContactReq
     * @param apiStringCallbackListener
     */
    void getContactParty(PartyContactReq partyContactReq, ApiStringCallbackListener apiStringCallbackListener);

    /**
     * 同意加入群
     * @param partyApplyAgreeReq
     * @param apiStringCallbackListener
     */
    void getPartyApplyAgree(PartyApplyAgreeReq partyApplyAgreeReq, ApiStringCallbackListener apiStringCallbackListener);

    /**
     * 退出群聊
     * @param memberExitReq
     * @param apiStringCallbackListener
     */
    void getMemberExit(MemberExitReq memberExitReq, ApiStringCallbackListener apiStringCallbackListener);
}
