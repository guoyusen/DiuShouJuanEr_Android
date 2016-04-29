package com.bili.diushoujuaner.model.apihelper.api;

import com.android.volley.Request;
import com.bili.diushoujuaner.model.apihelper.DataLoader;
import com.bili.diushoujuaner.model.apihelper.callback.ApiFileCallbackListener;
import com.bili.diushoujuaner.model.apihelper.callback.ApiStringCallbackListener;
import com.bili.diushoujuaner.model.apihelper.request.AccountUpdateReq;
import com.bili.diushoujuaner.model.apihelper.request.AutographModifyReq;
import com.bili.diushoujuaner.model.apihelper.request.BatchMemberAddReq;
import com.bili.diushoujuaner.model.apihelper.request.CommentAddReq;
import com.bili.diushoujuaner.model.apihelper.request.CommentRemoveReq;
import com.bili.diushoujuaner.model.apihelper.request.ContactAddInfoReq;
import com.bili.diushoujuaner.model.apihelper.request.ContactsSearchReq;
import com.bili.diushoujuaner.model.apihelper.request.FeedBackReq;
import com.bili.diushoujuaner.model.apihelper.request.FriendApplyReq;
import com.bili.diushoujuaner.model.apihelper.request.FriendAgreeReq;
import com.bili.diushoujuaner.model.apihelper.request.FriendDeleteReq;
import com.bili.diushoujuaner.model.apihelper.request.MemberExitReq;
import com.bili.diushoujuaner.model.apihelper.request.MemberForceExitReq;
import com.bili.diushoujuaner.model.apihelper.request.MemberNameUpdateReq;
import com.bili.diushoujuaner.model.apihelper.request.PartyAddReq;
import com.bili.diushoujuaner.model.apihelper.request.PartyApplyAgreeReq;
import com.bili.diushoujuaner.model.apihelper.request.PartyApplyReq;
import com.bili.diushoujuaner.model.apihelper.request.PartyContactReq;
import com.bili.diushoujuaner.model.apihelper.request.PartyHeadUpdateReq;
import com.bili.diushoujuaner.model.apihelper.request.PartyIntroduceUpdateReq;
import com.bili.diushoujuaner.model.apihelper.request.PartyNameUpdateReq;
import com.bili.diushoujuaner.model.apihelper.request.PartyUnGroupReq;
import com.bili.diushoujuaner.model.apihelper.request.RecallListReq;
import com.bili.diushoujuaner.model.apihelper.request.RecallPublishReq;
import com.bili.diushoujuaner.model.apihelper.request.RecallRemoveReq;
import com.bili.diushoujuaner.model.apihelper.request.RecallSerialReq;
import com.bili.diushoujuaner.model.apihelper.request.RecentRecallReq;
import com.bili.diushoujuaner.model.apihelper.request.RemarkUpdateReq;
import com.bili.diushoujuaner.model.apihelper.request.ResponAddReq;
import com.bili.diushoujuaner.model.apihelper.request.ResponRemoveReq;
import com.bili.diushoujuaner.model.apihelper.request.UserAccountReq;
import com.bili.diushoujuaner.model.apihelper.request.UserInfoReq;
import com.bili.diushoujuaner.model.apihelper.request.VerifyReq;
import com.bili.diushoujuaner.utils.CommonUtil;
import com.bili.diushoujuaner.utils.StringUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by BiLi on 2016/3/11.
 */
public class ApiAction implements Api {

    private static DataLoader dataLoader;
    private static ApiAction apiAction;

    public static void initialize() {
        dataLoader = new DataLoader();
        apiAction = new ApiAction();
    }

    public static synchronized ApiAction getInstance(){
        if(apiAction == null){
            throw new NullPointerException("ApiAction was not initialized!");
        }
        return apiAction;
    }

    @Override
    public void getUserLogin(UserAccountReq userAccountReq, ApiStringCallbackListener apiStringCallbackListener) {
        dataLoader.processStringRequest(Request.Method.POST, Api.getUserLogin, CommonUtil.ConvertObjToMap(userAccountReq), apiStringCallbackListener);
    }

    @Override
    public void getUserInfo(ApiStringCallbackListener apiStringCallbackListener) {
        dataLoader.processStringRequest(Request.Method.GET, Api.getUserInfo, null, apiStringCallbackListener);
    }

    @Override
    public void getContactList(ApiStringCallbackListener apiStringCallbackListener) {
        dataLoader.processStringRequest(Request.Method.GET, Api.getContactList, null, apiStringCallbackListener);
    }

    @Override
    public void getRecallList(RecallListReq recallListReq, ApiStringCallbackListener apiStringCallbackListener) {
        dataLoader.processStringRequest(Request.Method.GET, StringUtil.getCompleteUrl(Api.getRecallList, recallListReq), null, apiStringCallbackListener);
    }

    @Override
    public void getGoodAdd(long recallNo, ApiStringCallbackListener apiStringCallbackListener) {
        Map<String, String> params = new HashMap<>();
        params.put("recallNo",recallNo + "");

        dataLoader.processStringRequest(Request.Method.POST, Api.getGoodAdd, params, apiStringCallbackListener);
    }

    @Override
    public void getGoodRemove(long recallNo, ApiStringCallbackListener apiStringCallbackListener) {
        Map<String, String> params = new HashMap<>();
        params.put("recallNo",recallNo + "");

        dataLoader.processStringRequest(Request.Method.POST, Api.getGoodRemove, params, apiStringCallbackListener);
    }

    @Override
    public void getCommentAdd(CommentAddReq commentAddReq, ApiStringCallbackListener apiStringCallbackListener) {
        dataLoader.processStringRequest(Request.Method.POST, Api.getCommentAdd, CommonUtil.ConvertObjToMap(commentAddReq), apiStringCallbackListener);
    }

    @Override
    public void getResponAdd(ResponAddReq responAddReq, ApiStringCallbackListener apiStringCallbackListener) {
        dataLoader.processStringRequest(Request.Method.POST, Api.getResponAdd, CommonUtil.ConvertObjToMap(responAddReq), apiStringCallbackListener);
    }

    @Override
    public void getCommentRemove(CommentRemoveReq commentRemoveReq, ApiStringCallbackListener apiStringCallbackListener) {
        dataLoader.processStringRequest(Request.Method.POST, Api.getCommentRemove, CommonUtil.ConvertObjToMap(commentRemoveReq), apiStringCallbackListener);
    }

    @Override
    public void getResponRemove(ResponRemoveReq responRemoveReq, ApiStringCallbackListener apiStringCallbackListener) {
        dataLoader.processStringRequest(Request.Method.POST, Api.getResponRemove, CommonUtil.ConvertObjToMap(responRemoveReq), apiStringCallbackListener);
    }

    @Override
    public void getContactInfo(ContactAddInfoReq contactAddInfoReq, ApiStringCallbackListener apiStringCallbackListener) {
        dataLoader.processStringRequest(Request.Method.GET, StringUtil.getCompleteUrl(Api.getContactInfo, contactAddInfoReq), null, apiStringCallbackListener);
    }

    @Override
    public void getRecentRecall(RecentRecallReq recentRecallReq, ApiStringCallbackListener apiStringCallbackListener) {
        dataLoader.processStringRequest(Request.Method.GET, StringUtil.getCompleteUrl(Api.getRecentRecall, recentRecallReq), null, apiStringCallbackListener);
    }

    @Override
    public void getAutographModify(AutographModifyReq autographModifyReq, ApiStringCallbackListener apiStringCallbackListener) {
        dataLoader.processStringRequest(Request.Method.POST, Api.getAutographModify, CommonUtil.ConvertObjToMap(autographModifyReq), apiStringCallbackListener);
    }

    @Override
    public void getFeedBackAdd(FeedBackReq feedBackReq, ApiStringCallbackListener apiStringCallbackListener) {
        dataLoader.processStringRequest(Request.Method.POST, Api.getFeedBackAdd, CommonUtil.ConvertObjToMap(feedBackReq), apiStringCallbackListener);
    }

    @Override
    public void getUserInfoUpdate(UserInfoReq userInfoReq, ApiStringCallbackListener apiStringCallbackListener) {
        dataLoader.processStringRequest(Request.Method.POST, Api.getUserInfoUpdate, CommonUtil.ConvertObjToMap(userInfoReq), apiStringCallbackListener);
    }

    @Override
    public void getVerifyCode(VerifyReq verifyReq, ApiStringCallbackListener apiStringCallbackListener) {
        dataLoader.processStringRequest(Request.Method.POST, Api.getVerifyCode, CommonUtil.ConvertObjToMap(verifyReq), apiStringCallbackListener);
    }

    @Override
    public void getAcountRegist(AccountUpdateReq accountUpdateReq, ApiStringCallbackListener apiStringCallbackListener) {
        dataLoader.processStringRequest(Request.Method.POST, Api.getAcountRegist, CommonUtil.ConvertObjToMap(accountUpdateReq), apiStringCallbackListener);
    }

    @Override
    public void getAcountReset(AccountUpdateReq accountUpdateReq, ApiStringCallbackListener apiStringCallbackListener) {
        dataLoader.processStringRequest(Request.Method.POST, Api.getAcountReset, CommonUtil.ConvertObjToMap(accountUpdateReq), apiStringCallbackListener);
    }

    @Override
    public void getLogout(ApiStringCallbackListener apiStringCallbackListener) {
        dataLoader.processStringRequest(Request.Method.GET, Api.getLogout, null, apiStringCallbackListener);
    }

    @Override
    public void getRecallRemove(RecallRemoveReq recallRemoveReq, ApiStringCallbackListener apiStringCallbackListener) {
        dataLoader.processStringRequest(Request.Method.POST, Api.getRecallRemove, CommonUtil.ConvertObjToMap(recallRemoveReq), apiStringCallbackListener);
    }

    @Override
    public void getHeadUpdate(String path, ApiFileCallbackListener apiFileCallbackListener){
        dataLoader.processFileUpload(Api.getHeadUpdate, null, "file", path, apiFileCallbackListener);
    }

    @Override
    public void getWallpaperUpdate(String path, ApiFileCallbackListener apiFileCallbackListener) {
        dataLoader.processFileUpload(Api.getWallpaperUpdate, null, "file", path, apiFileCallbackListener);
    }

    @Override
    public void getRecallPicUpload(RecallSerialReq recallSerialReq, String path, ApiFileCallbackListener apiFileCallbackListener) {
        dataLoader.processFileUpload(Api.getRecallPicUpload, CommonUtil.ConvertObjToMap(recallSerialReq), "file",path, apiFileCallbackListener);
    }

    @Override
    public void getRecallPublish(RecallPublishReq recallPublishReq, ApiStringCallbackListener apiStringCallbackListener) {
        dataLoader.processStringRequest(Request.Method.POST, Api.getRecallPublish, CommonUtil.ConvertObjToMap(recallPublishReq), apiStringCallbackListener);
    }

    @Override
    public void getOffMsg(ApiStringCallbackListener apiStringCallbackListener) {
        dataLoader.processStringRequest(Request.Method.GET, Api.getOffMsg, null, apiStringCallbackListener);
    }

    @Override
    public void getMemberNameUpdate(MemberNameUpdateReq memberNameUpdateReq, ApiStringCallbackListener apiStringCallbackListener) {
        dataLoader.processStringRequest(Request.Method.POST, Api.getMemberNameUpdate, CommonUtil.ConvertObjToMap(memberNameUpdateReq), apiStringCallbackListener);
    }

    @Override
    public void getPartyHeadUpdate(PartyHeadUpdateReq partyHeadUpdateReq, String path, ApiFileCallbackListener apiFileCallbackListener) {
        dataLoader.processFileUpload(Api.getPartyHeadUpdate, CommonUtil.ConvertObjToMap(partyHeadUpdateReq), "file",path, apiFileCallbackListener);
    }

    @Override
    public void getPartyNameUpdate(PartyNameUpdateReq partyNameUpdateReq, ApiStringCallbackListener apiStringCallbackListener) {
        dataLoader.processStringRequest(Request.Method.POST, Api.getPartyNameUpdate, CommonUtil.ConvertObjToMap(partyNameUpdateReq), apiStringCallbackListener);
    }

    @Override
    public void getPartyIntroduceUpdate(PartyIntroduceUpdateReq partyIntroduceUpdateReq, ApiStringCallbackListener apiStringCallbackListener) {
        dataLoader.processStringRequest(Request.Method.POST, Api.getPartyIntroduceUpdate, CommonUtil.ConvertObjToMap(partyIntroduceUpdateReq), apiStringCallbackListener);
    }

    @Override
    public void getContactsSearch(ContactsSearchReq contactsSearchReq, ApiStringCallbackListener apiStringCallbackListener) {
        dataLoader.processStringRequest(Request.Method.GET, StringUtil.getCompleteUrl(Api.getContactsSearch, contactsSearchReq), null, apiStringCallbackListener);
    }

    @Override
    public void getFriendApply(FriendApplyReq friendApplyReq, ApiStringCallbackListener apiStringCallbackListener) {
        dataLoader.processStringRequest(Request.Method.POST, Api.getFriendApply, CommonUtil.ConvertObjToMap(friendApplyReq), apiStringCallbackListener);
    }

    @Override
    public void getFriendAgree(FriendAgreeReq friendAgreeReq, ApiStringCallbackListener apiStringCallbackListener) {
        dataLoader.processStringRequest(Request.Method.POST, Api.getFriendAgree, CommonUtil.ConvertObjToMap(friendAgreeReq), apiStringCallbackListener);
    }

    @Override
    public void getfriendRemarkUpdate(RemarkUpdateReq remarkUpdateReq, ApiStringCallbackListener apiStringCallbackListener) {
        dataLoader.processStringRequest(Request.Method.POST, Api.getFriendRemarkUpdate, CommonUtil.ConvertObjToMap(remarkUpdateReq), apiStringCallbackListener);
    }

    @Override
    public void getFriendDelete(FriendDeleteReq friendDeleteReq, ApiStringCallbackListener apiStringCallbackListener) {
        dataLoader.processStringRequest(Request.Method.POST, Api.getFriendDelete, CommonUtil.ConvertObjToMap(friendDeleteReq), apiStringCallbackListener);
    }

    @Override
    public void getPartyAdd(PartyAddReq partyAddReq, String path, ApiFileCallbackListener apiFileCallbackListener) {
        dataLoader.processFileUpload(Api.getPartyAdd, CommonUtil.ConvertObjToMap(partyAddReq), "file", path, apiFileCallbackListener);
    }

    @Override
    public void getPartyApply(PartyApplyReq partyApplyReq, ApiStringCallbackListener apiStringCallbackListener) {
        dataLoader.processStringRequest(Request.Method.POST, Api.getPartyApply, CommonUtil.ConvertObjToMap(partyApplyReq), apiStringCallbackListener);
    }

    @Override
    public void getContactParty(PartyContactReq partyContactReq, ApiStringCallbackListener apiStringCallbackListener) {
        dataLoader.processStringRequest(Request.Method.GET, StringUtil.getCompleteUrl(Api.getContactParty, partyContactReq), null, apiStringCallbackListener);
    }

    @Override
    public void getPartyApplyAgree(PartyApplyAgreeReq partyApplyAgreeReq, ApiStringCallbackListener apiStringCallbackListener) {
        dataLoader.processStringRequest(Request.Method.POST, Api.getPartyApplyAgree, CommonUtil.ConvertObjToMap(partyApplyAgreeReq), apiStringCallbackListener);
    }

    @Override
    public void getMemberExit(MemberExitReq memberExitReq, ApiStringCallbackListener apiStringCallbackListener) {
        dataLoader.processStringRequest(Request.Method.POST, Api.getMemberExit, CommonUtil.ConvertObjToMap(memberExitReq), apiStringCallbackListener);
    }

    @Override
    public void getMemberForceExit(MemberForceExitReq memberForceExitReq, ApiStringCallbackListener apiStringCallbackListener) {
        dataLoader.processStringRequest(Request.Method.POST, Api.getMemberForceExit, CommonUtil.ConvertObjToMap(memberForceExitReq), apiStringCallbackListener);
    }

    @Override
    public void getMembersAddToParty(BatchMemberAddReq batchMemberAddReq, ApiStringCallbackListener apiStringCallbackListener) {
        dataLoader.processStringRequest(Request.Method.POST, Api.getMembersAddToParty, CommonUtil.ConvertObjToMap(batchMemberAddReq), apiStringCallbackListener);
    }

    @Override
    public void getPartyUnGroup(PartyUnGroupReq partyUnGroupReq, ApiStringCallbackListener apiStringCallbackListener) {
        dataLoader.processStringRequest(Request.Method.POST, Api.getPartyUnGroup, CommonUtil.ConvertObjToMap(partyUnGroupReq), apiStringCallbackListener);
    }
}
