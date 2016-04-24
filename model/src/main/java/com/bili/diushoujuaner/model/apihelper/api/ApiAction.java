package com.bili.diushoujuaner.model.apihelper.api;

import com.android.volley.Request;
import com.bili.diushoujuaner.model.apihelper.DataLoader;
import com.bili.diushoujuaner.model.apihelper.callback.ApiFileCallbackListener;
import com.bili.diushoujuaner.model.apihelper.callback.ApiStringCallbackListener;
import com.bili.diushoujuaner.model.apihelper.request.AcountUpdateReq;
import com.bili.diushoujuaner.model.apihelper.request.AutographModifyReq;
import com.bili.diushoujuaner.model.apihelper.request.CommentAddReq;
import com.bili.diushoujuaner.model.apihelper.request.CommentRemoveReq;
import com.bili.diushoujuaner.model.apihelper.request.ContactInfoReq;
import com.bili.diushoujuaner.model.apihelper.request.ContactsSearchReq;
import com.bili.diushoujuaner.model.apihelper.request.FeedBackReq;
import com.bili.diushoujuaner.model.apihelper.request.FriendAddReq;
import com.bili.diushoujuaner.model.apihelper.request.FriendAgreeReq;
import com.bili.diushoujuaner.model.apihelper.request.MemberNameUpdateReq;
import com.bili.diushoujuaner.model.apihelper.request.PartyHeadUpdateReq;
import com.bili.diushoujuaner.model.apihelper.request.PartyIntroduceUpdateReq;
import com.bili.diushoujuaner.model.apihelper.request.PartyNameUpdateReq;
import com.bili.diushoujuaner.model.apihelper.request.RecallListReq;
import com.bili.diushoujuaner.model.apihelper.request.RecallPublishReq;
import com.bili.diushoujuaner.model.apihelper.request.RecallRemoveReq;
import com.bili.diushoujuaner.model.apihelper.request.RecallSerialReq;
import com.bili.diushoujuaner.model.apihelper.request.RecentRecallReq;
import com.bili.diushoujuaner.model.apihelper.request.ResponAddReq;
import com.bili.diushoujuaner.model.apihelper.request.ResponRemoveReq;
import com.bili.diushoujuaner.model.apihelper.request.UserAccountReq;
import com.bili.diushoujuaner.model.apihelper.request.UserInfoReq;
import com.bili.diushoujuaner.model.apihelper.request.VerifyReq;
import com.bili.diushoujuaner.utils.Common;

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
        dataLoader.processStringRequest(Request.Method.POST, Api.getUserLogin, Common.ConvertObjToMap(userAccountReq), apiStringCallbackListener);
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
        dataLoader.processStringRequest(Request.Method.GET, Common.getCompleteUrl(Api.getRecallList, recallListReq), null, apiStringCallbackListener);
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
        dataLoader.processStringRequest(Request.Method.POST, Api.getCommentAdd, Common.ConvertObjToMap(commentAddReq), apiStringCallbackListener);
    }

    @Override
    public void getResponAdd(ResponAddReq responAddReq, ApiStringCallbackListener apiStringCallbackListener) {
        dataLoader.processStringRequest(Request.Method.POST, Api.getResponAdd, Common.ConvertObjToMap(responAddReq), apiStringCallbackListener);
    }

    @Override
    public void getCommentRemove(CommentRemoveReq commentRemoveReq, ApiStringCallbackListener apiStringCallbackListener) {
        dataLoader.processStringRequest(Request.Method.POST, Api.getCommentRemove, Common.ConvertObjToMap(commentRemoveReq), apiStringCallbackListener);
    }

    @Override
    public void getResponRemove(ResponRemoveReq responRemoveReq, ApiStringCallbackListener apiStringCallbackListener) {
        dataLoader.processStringRequest(Request.Method.POST, Api.getResponRemove, Common.ConvertObjToMap(responRemoveReq), apiStringCallbackListener);
    }

    @Override
    public void getContactInfo(ContactInfoReq contactInfoReq, ApiStringCallbackListener apiStringCallbackListener) {
        dataLoader.processStringRequest(Request.Method.GET, Common.getCompleteUrl(Api.getContactInfo, contactInfoReq), null, apiStringCallbackListener);
    }

    @Override
    public void getRecentRecall(RecentRecallReq recentRecallReq, ApiStringCallbackListener apiStringCallbackListener) {
        dataLoader.processStringRequest(Request.Method.GET, Common.getCompleteUrl(Api.getRecentRecall, recentRecallReq), null, apiStringCallbackListener);
    }

    @Override
    public void getAutographModify(AutographModifyReq autographModifyReq, ApiStringCallbackListener apiStringCallbackListener) {
        dataLoader.processStringRequest(Request.Method.POST, Api.getAutographModify, Common.ConvertObjToMap(autographModifyReq), apiStringCallbackListener);
    }

    @Override
    public void getFeedBackAdd(FeedBackReq feedBackReq, ApiStringCallbackListener apiStringCallbackListener) {
        dataLoader.processStringRequest(Request.Method.POST, Api.getFeedBackAdd, Common.ConvertObjToMap(feedBackReq), apiStringCallbackListener);
    }

    @Override
    public void getUserInfoUpdate(UserInfoReq userInfoReq, ApiStringCallbackListener apiStringCallbackListener) {
        dataLoader.processStringRequest(Request.Method.POST, Api.getUserInfoUpdate, Common.ConvertObjToMap(userInfoReq), apiStringCallbackListener);
    }

    @Override
    public void getVerifyCode(VerifyReq verifyReq, ApiStringCallbackListener apiStringCallbackListener) {
        dataLoader.processStringRequest(Request.Method.POST, Api.getVerifyCode, Common.ConvertObjToMap(verifyReq), apiStringCallbackListener);
    }

    @Override
    public void getAcountRegist(AcountUpdateReq acountUpdateReq, ApiStringCallbackListener apiStringCallbackListener) {
        dataLoader.processStringRequest(Request.Method.POST, Api.getAcountRegist, Common.ConvertObjToMap(acountUpdateReq), apiStringCallbackListener);
    }

    @Override
    public void getAcountReset(AcountUpdateReq acountUpdateReq, ApiStringCallbackListener apiStringCallbackListener) {
        dataLoader.processStringRequest(Request.Method.POST, Api.getAcountReset, Common.ConvertObjToMap(acountUpdateReq), apiStringCallbackListener);
    }

    @Override
    public void getLogout(ApiStringCallbackListener apiStringCallbackListener) {
        dataLoader.processStringRequest(Request.Method.GET, Api.getLogout, null, apiStringCallbackListener);
    }

    @Override
    public void getRecallRemove(RecallRemoveReq recallRemoveReq, ApiStringCallbackListener apiStringCallbackListener) {
        dataLoader.processStringRequest(Request.Method.POST, Api.getRecallRemove, Common.ConvertObjToMap(recallRemoveReq), apiStringCallbackListener);
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
        dataLoader.processFileUpload(Api.getRecallPicUpload,Common.ConvertObjToMap(recallSerialReq), "file",path, apiFileCallbackListener);
    }

    @Override
    public void getRecallPublish(RecallPublishReq recallPublishReq, ApiStringCallbackListener apiStringCallbackListener) {
        dataLoader.processStringRequest(Request.Method.POST, Api.getRecallPublish, Common.ConvertObjToMap(recallPublishReq), apiStringCallbackListener);
    }

    @Override
    public void getOffMsg(ApiStringCallbackListener apiStringCallbackListener) {
        dataLoader.processStringRequest(Request.Method.GET, Api.getOffMsg, null, apiStringCallbackListener);
    }

    @Override
    public void getMemberNameUpdate(MemberNameUpdateReq memberNameUpdateReq, ApiStringCallbackListener apiStringCallbackListener) {
        dataLoader.processStringRequest(Request.Method.POST, Api.getMemberNameUpdate, Common.ConvertObjToMap(memberNameUpdateReq), apiStringCallbackListener);
    }

    @Override
    public void getPartyHeadUpdate(PartyHeadUpdateReq partyHeadUpdateReq, String path, ApiFileCallbackListener apiFileCallbackListener) {
        dataLoader.processFileUpload(Api.getPartyHeadUpdate,Common.ConvertObjToMap(partyHeadUpdateReq), "file",path, apiFileCallbackListener);
    }

    @Override
    public void getPartyNameUpdate(PartyNameUpdateReq partyNameUpdateReq, ApiStringCallbackListener apiStringCallbackListener) {
        dataLoader.processStringRequest(Request.Method.POST, Api.getPartyNameUpdate, Common.ConvertObjToMap(partyNameUpdateReq), apiStringCallbackListener);
    }

    @Override
    public void getPartyIntroduceUpdate(PartyIntroduceUpdateReq partyIntroduceUpdateReq, ApiStringCallbackListener apiStringCallbackListener) {
        dataLoader.processStringRequest(Request.Method.POST, Api.getPartyIntroduceUpdate, Common.ConvertObjToMap(partyIntroduceUpdateReq), apiStringCallbackListener);
    }

    @Override
    public void getContactsSearch(ContactsSearchReq contactsSearchReq, ApiStringCallbackListener apiStringCallbackListener) {
        dataLoader.processStringRequest(Request.Method.GET, Common.getCompleteUrl(Api.getContactsSearch, contactsSearchReq), null, apiStringCallbackListener);
    }

    @Override
    public void getFriendAdd(FriendAddReq friendAddReq, ApiStringCallbackListener apiStringCallbackListener) {
        dataLoader.processStringRequest(Request.Method.POST, Api.getFriendAdd, Common.ConvertObjToMap(friendAddReq), apiStringCallbackListener);
    }

    @Override
    public void getFriendAgree(FriendAgreeReq friendAgreeReq, ApiStringCallbackListener apiStringCallbackListener) {
        dataLoader.processStringRequest(Request.Method.POST, Api.getFriendAgree, Common.ConvertObjToMap(friendAgreeReq), apiStringCallbackListener);
    }
}
