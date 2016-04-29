package com.bili.diushoujuaner.model.actionhelper.action;

import android.content.Context;

import com.bili.diushoujuaner.model.actionhelper.IContactAction;
import com.bili.diushoujuaner.model.actionhelper.respon.ActionResponse;
import com.bili.diushoujuaner.model.apihelper.ApiResponse;
import com.bili.diushoujuaner.model.apihelper.api.ApiAction;
import com.bili.diushoujuaner.model.apihelper.callback.ApiFileCallbackListener;
import com.bili.diushoujuaner.model.apihelper.callback.ApiStringCallbackListener;
import com.bili.diushoujuaner.model.apihelper.request.BatchMemberAddReq;
import com.bili.diushoujuaner.model.apihelper.request.ContactAddInfoReq;
import com.bili.diushoujuaner.model.apihelper.request.ContactsSearchReq;
import com.bili.diushoujuaner.model.apihelper.request.FriendDeleteReq;
import com.bili.diushoujuaner.model.apihelper.request.MemberExitReq;
import com.bili.diushoujuaner.model.apihelper.request.MemberForceExitReq;
import com.bili.diushoujuaner.model.apihelper.request.MemberNameUpdateReq;
import com.bili.diushoujuaner.model.apihelper.request.PartyAddReq;
import com.bili.diushoujuaner.model.apihelper.request.PartyContactReq;
import com.bili.diushoujuaner.model.apihelper.request.PartyIntroduceUpdateReq;
import com.bili.diushoujuaner.model.apihelper.request.PartyNameUpdateReq;
import com.bili.diushoujuaner.model.apihelper.request.PartyUnGroupReq;
import com.bili.diushoujuaner.model.apihelper.request.RemarkUpdateReq;
import com.bili.diushoujuaner.model.cachehelper.ACache;
import com.bili.diushoujuaner.model.callback.ActionFileCallbackListener;
import com.bili.diushoujuaner.model.callback.ActionStringCallbackListener;
import com.bili.diushoujuaner.model.databasehelper.DBManager;
import com.bili.diushoujuaner.model.eventhelper.DeleteContactEvent;
import com.bili.diushoujuaner.model.eventhelper.NoticeAddMemberEvent;
import com.bili.diushoujuaner.model.eventhelper.RequestContactEvent;
import com.bili.diushoujuaner.model.eventhelper.UnGroupPartyEvent;
import com.bili.diushoujuaner.model.eventhelper.UpdateContactEvent;
import com.bili.diushoujuaner.model.eventhelper.UpdatePartyEvent;
import com.bili.diushoujuaner.model.eventhelper.UpdateRemarkEvent;
import com.bili.diushoujuaner.model.messagehelper.MessageServiceHandler;
import com.bili.diushoujuaner.model.messagehelper.Notifier;
import com.bili.diushoujuaner.model.preferhelper.CustomSessionPreference;
import com.bili.diushoujuaner.model.tempHelper.ChattingTemper;
import com.bili.diushoujuaner.model.tempHelper.ContactTemper;
import com.bili.diushoujuaner.utils.ConstantUtil;
import com.bili.diushoujuaner.utils.EntityUtil;
import com.bili.diushoujuaner.utils.GsonUtil;
import com.bili.diushoujuaner.utils.NoticeUtil;
import com.bili.diushoujuaner.utils.PinyinUtil;
import com.bili.diushoujuaner.utils.StringUtil;
import com.bili.diushoujuaner.utils.TimeUtil;
import com.bili.diushoujuaner.utils.comparator.ContactComparator;
import com.bili.diushoujuaner.utils.comparator.MemberVoComparator;
import com.bili.diushoujuaner.utils.entity.dto.ContactDto;
import com.bili.diushoujuaner.utils.entity.dto.MemberDto;
import com.bili.diushoujuaner.utils.entity.dto.UserDto;
import com.bili.diushoujuaner.utils.entity.po.Friend;
import com.bili.diushoujuaner.utils.entity.po.Member;
import com.bili.diushoujuaner.utils.entity.po.Party;
import com.bili.diushoujuaner.utils.entity.po.User;
import com.bili.diushoujuaner.utils.entity.vo.FriendVo;
import com.bili.diushoujuaner.utils.entity.vo.MemberVo;
import com.bili.diushoujuaner.utils.entity.vo.MessageVo;
import com.bili.diushoujuaner.utils.entity.vo.PartyVo;
import com.google.gson.reflect.TypeToken;
import com.nanotasks.BackgroundWork;
import com.nanotasks.Completion;
import com.nanotasks.Tasks;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by BiLi on 2016/3/15.
 */
public class ContactAction implements IContactAction{

    private static ContactAction contactAction;
    private Context context;

    public ContactAction(Context context) {
        this.context = context;
    }

    public static synchronized ContactAction getInstance(Context context){
        if(contactAction == null){
            contactAction = new ContactAction(context);
        }
        return contactAction;
    }

    @Override
    public void getPartyUnGroup(final PartyUnGroupReq partyUnGroupReq, final ActionStringCallbackListener<ActionResponse<Void>> actionStringCallbackListener) {
        ApiAction.getInstance().getPartyUnGroup(partyUnGroupReq, new ApiStringCallbackListener() {
            @Override
            public void onSuccess(final String data) {
                Tasks.executeInBackground(context, new BackgroundWork<ActionResponse<Void>>() {
                    @Override
                    public ActionResponse<Void> doInBackground() throws Exception {
                        ApiResponse<Void> result = GsonUtil.getInstance().fromJson(data, new TypeToken<ApiResponse<Void>>(){}.getType());
                        if(result.isLegal()){
                            DBManager.getInstance().deleteParty(partyUnGroupReq.getPartyNo());
                            ChattingTemper.getInstance().deleteChattingVo(partyUnGroupReq.getPartyNo(), ConstantUtil.CHAT_PAR);
                            EventBus.getDefault().post(new UpdateContactEvent());
                            EventBus.getDefault().post(new UnGroupPartyEvent(partyUnGroupReq.getPartyNo()));
                        }
                        return ActionResponse.getActionResponFromApiRespon(result);
                    }
                }, new Completion<ActionResponse<Void>>() {
                    @Override
                    public void onSuccess(Context context, ActionResponse<Void> result) {
                        actionStringCallbackListener.onSuccess(result);
                    }

                    @Override
                    public void onError(Context context, Exception e) {
                        actionStringCallbackListener.onSuccess(ActionResponse.<Void>getActionResponError());
                    }
                });
            }

            @Override
            public void onFailure(int errorCode) {
                actionStringCallbackListener.onFailure(errorCode);
            }
        });
    }

    //userNo用户邀请memberNoList加入了partyNo
    public void saveNewMembersInfo(final ContactDto contactDto,final long partyNo, final long userNo, final boolean isLocal){
        if(DBManager.getInstance().getParty(partyNo) == null){
            getWholeParty(partyNo, contactDto, userNo, isLocal);
        }else{
            DBManager.getInstance().saveMemberList(getMemberListFromContactDto(contactDto));
            noticeNewMembers(contactDto, userNo, isLocal);
        }
    }

    private void noticeNewMembers(ContactDto contactDto, long userNo, boolean isLocal){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("我邀请 ");
        List<MemberDto> memberList = contactDto.getMemberList();
        for(int i = 0, len = memberList.size(); i < len; i++){
            stringBuilder.append(memberList.get(i).getMemberName());
            if(i < len - 1){
                stringBuilder.append(",");
            }
        }
        stringBuilder.append(" 加入了群聊");
        MessageVo messageVo = EntityUtil.getMessageVoForNoticeNewMember(contactDto.getContNo(), userNo, stringBuilder.toString(), TimeUtil.getCurrentTimeYYMMDD_HHMMSS());
        DBManager.getInstance().updateMemberRecent(messageVo.getToNo(), true);
        messageVo = DBManager.getInstance().saveMessage(messageVo);
        NoticeUtil.getInstance().playNotice();

        if(isLocal){
            ChattingTemper.getInstance().addChattingVoFromServer(messageVo);
            EventBus.getDefault().post(new UpdateContactEvent());
            EventBus.getDefault().post(new NoticeAddMemberEvent(messageVo));
        }else{
            // 邀请多人加入群，par
            Notifier.showNotification(messageVo);
            MessageServiceHandler.getInstance().sendMessageToClient(ConstantUtil.HANDLER_MEMBER_BATCH_ADD, messageVo);
        }
    }

    private void getWholeParty(long partyNo, final ContactDto contactDto, final long userNo, final boolean isLocal){
        ApiAction.getInstance().getContactParty(new PartyContactReq(partyNo), new ApiStringCallbackListener() {
            @Override
            public void onSuccess(String data) {
                ApiResponse<ContactDto> result = GsonUtil.getInstance().fromJson(data, new TypeToken<ApiResponse<ContactDto>>(){}.getType());
                if(result.isLegal() && result.getData().getType() == ConstantUtil.CONTACT_PARTY){
                    DBManager.getInstance().saveParty(EntityUtil.getPartyFromContactDto(result.getData()));
                    DBManager.getInstance().saveMemberList(getMemberListFromContactDto(result.getData()));
                    noticeNewMembers(contactDto, userNo, isLocal);
                }
            }

            @Override
            public void onFailure(int errorCode) {
            }
        });
    }

    @Override
    public void getMembersAddToParty(final BatchMemberAddReq batchMemberAddReq, final ActionStringCallbackListener<ActionResponse<Void>> actionStringCallbackListener) {
        ApiAction.getInstance().getMembersAddToParty(batchMemberAddReq, new ApiStringCallbackListener() {
            @Override
            public void onSuccess(final String data) {
                Tasks.executeInBackground(context, new BackgroundWork<ActionResponse<Void>>() {
                    @Override
                    public ActionResponse<Void> doInBackground() throws Exception {
                        ApiResponse<ContactDto> result = GsonUtil.getInstance().fromJson(data, new TypeToken<ApiResponse<ContactDto>>(){}.getType());
                        if(result.isLegal()){
                            saveNewMembersInfo(result.getData(), batchMemberAddReq.getPartyNo(), CustomSessionPreference.getInstance().getCustomSession().getUserNo(), true);
                        }
                        return ActionResponse.getActionRespon(null);
                    }
                }, new Completion<ActionResponse<Void>>() {
                    @Override
                    public void onSuccess(Context context, ActionResponse<Void> result) {
                        actionStringCallbackListener.onSuccess(result);
                    }

                    @Override
                    public void onError(Context context, Exception e) {
                        actionStringCallbackListener.onSuccess(ActionResponse.<Void>getActionResponError());
                    }
                });
            }

            @Override
            public void onFailure(int errorCode) {
                actionStringCallbackListener.onFailure(errorCode);
            }
        });
    }

    @Override
    public void getMemberForceExit(final MemberForceExitReq memberForceExitReq, final ActionStringCallbackListener<ActionResponse<List<MemberVo>>> actionStringCallbackListener) {
        ApiAction.getInstance().getMemberForceExit(memberForceExitReq, new ApiStringCallbackListener() {
            @Override
            public void onSuccess(final String data) {
                Tasks.executeInBackground(context, new BackgroundWork<ActionResponse<List<MemberVo>>>() {
                    @Override
                    public ActionResponse<List<MemberVo>> doInBackground() throws Exception {
                        ApiResponse<Void> result = GsonUtil.getInstance().fromJson(data, new TypeToken<ApiResponse<Void>>(){}.getType());
                        if(result.isLegal()){
                            DBManager.getInstance().deleteMember(memberForceExitReq.getPartyNo(), memberForceExitReq.getMemberNo());
                            DBManager.getInstance().deletePartyChat(memberForceExitReq.getPartyNo(), memberForceExitReq.getMemberNo());
                            EventBus.getDefault().post(new UpdateContactEvent());
                            EventBus.getDefault().post(new UpdatePartyEvent(memberForceExitReq.getPartyNo(), memberForceExitReq.getMemberNo(),"", ConstantUtil.CHAT_PARTY_MEMBER_EXIT));
                        }
                        List<MemberVo> memberVoList = DBManager.getInstance().getMemberVoList(memberForceExitReq.getPartyNo());
                        if(memberVoList.size() == 1){
                            memberVoList.get(0).setSortLetter("★");
                        }else{
                            Collections.sort(memberVoList, new MemberVoComparator());
                        }
                        return ActionResponse.getActionRespon(memberVoList);
                    }
                }, new Completion<ActionResponse<List<MemberVo>>>() {
                    @Override
                    public void onSuccess(Context context, ActionResponse<List<MemberVo>> result) {
                        actionStringCallbackListener.onSuccess(result);
                    }

                    @Override
                    public void onError(Context context, Exception e) {
                        actionStringCallbackListener.onSuccess(ActionResponse.<List<MemberVo>>getActionResponError());
                    }
                });
            }

            @Override
            public void onFailure(int errorCode) {
                actionStringCallbackListener.onFailure(errorCode);
            }
        });
    }

    @Override
    public void getMemberVoListFromLocal(final long partyNo, final ActionStringCallbackListener<ActionResponse<List<MemberVo>>> actionStringCallbackListener) {
        Tasks.executeInBackground(context, new BackgroundWork<ActionResponse<List<MemberVo>>>() {
            @Override
            public ActionResponse<List<MemberVo>> doInBackground() throws Exception {
                List<MemberVo> memberVoList = DBManager.getInstance().getMemberVoList(partyNo);
                if(memberVoList.size() == 1){
                    memberVoList.get(0).setSortLetter("★");
                }else{
                    Collections.sort(memberVoList, new MemberVoComparator());
                }
                return ActionResponse.getActionRespon(memberVoList);
            }
        }, new Completion<ActionResponse<List<MemberVo>>>() {
            @Override
            public void onSuccess(Context context, ActionResponse<List<MemberVo>> result) {
                actionStringCallbackListener.onSuccess(result);
            }

            @Override
            public void onError(Context context, Exception e) {
                actionStringCallbackListener.onSuccess(ActionResponse.<List<MemberVo>>getActionResponError());
            }
        });
    }

    @Override
    public void getMemberExit(final MemberExitReq memberExitReq, final ActionStringCallbackListener<ActionResponse<Void>> actionStringCallbackListener) {
        ApiAction.getInstance().getMemberExit(memberExitReq, new ApiStringCallbackListener() {
            @Override
            public void onSuccess(final String data) {
                Tasks.executeInBackground(context, new BackgroundWork<ActionResponse<Void>>() {
                    @Override
                    public ActionResponse<Void> doInBackground() throws Exception {
                        ApiResponse<Void> result = GsonUtil.getInstance().fromJson(data, new TypeToken<ApiResponse<Void>>(){}.getType());
                        if(result.isLegal()){
                            DBManager.getInstance().deleteParty(memberExitReq.getPartyNo());
                            EventBus.getDefault().post(new UpdateContactEvent());
                            EventBus.getDefault().post(new UpdatePartyEvent(memberExitReq.getPartyNo(),CustomSessionPreference.getInstance().getCustomSession().getUserNo(),"", ConstantUtil.CHAT_PARTY_MEMBER_EXIT));
                        }
                        return ActionResponse.getActionResponFromApiRespon(result);
                    }
                }, new Completion<ActionResponse<Void>>() {
                    @Override
                    public void onSuccess(Context context, ActionResponse<Void> result) {
                        actionStringCallbackListener.onSuccess(result);
                    }

                    @Override
                    public void onError(Context context, Exception e) {
                        actionStringCallbackListener.onSuccess(ActionResponse.<Void>getActionResponError());
                    }
                });
            }

            @Override
            public void onFailure(int errorCode) {
                actionStringCallbackListener.onFailure(errorCode);
            }
        });
    }

    @Override
    public void getWholePartyInfo(final long partyNo, final long memberNo, final String time, final boolean isLocal) {
        // 全量获取群组信息，更新本地数据库和界面
        ApiAction.getInstance().getContactParty(new PartyContactReq(partyNo), new ApiStringCallbackListener() {
            @Override
            public void onSuccess(String data) {
                ApiResponse<ContactDto> result = GsonUtil.getInstance().fromJson(data, new TypeToken<ApiResponse<ContactDto>>(){}.getType());
                if(result.isLegal() && result.getData().getType() == ConstantUtil.CONTACT_PARTY){
                    DBManager.getInstance().saveParty(EntityUtil.getPartyFromContactDto(result.getData()));
                    DBManager.getInstance().saveMemberList(getMemberListFromContactDto(result.getData()));
                    if(isLocal){
                        EventBus.getDefault().post(new UpdateContactEvent());
                        noticeClientNewMember(partyNo, memberNo, time);
                    }else{
                        //不是从service发送过来执行，不需要发出通知
                        noticeServiceNewMember(partyNo, memberNo, time);
                    }
                }
            }

            @Override
            public void onFailure(int errorCode) {
            }
        });
    }

    private void noticeServiceNewMember(long partyNo, long memberNo, String time){
        MessageVo messageVo = EntityUtil.getMessageVoForNoticeNewMember(partyNo, memberNo, "我加入了群聊", time);
        DBManager.getInstance().updateMemberRecent(messageVo.getToNo(), true);
        messageVo = DBManager.getInstance().saveMessage(messageVo);
        NoticeUtil.getInstance().playNotice();
        Notifier.showNotification(messageVo);
        MessageServiceHandler.getInstance().sendMessageToClient(ConstantUtil.HANDLER_PARTY_APPLY_AGREE, messageVo);
    }

    private void noticeClientNewMember(long partyNo, long memberNo, String time){
        MessageVo messageVo = EntityUtil.getMessageVoForNoticeNewMember(partyNo, memberNo, "我加入了群聊", time);
        ChattingTemper.getInstance().addChattingVoFromServer(messageVo);
        DBManager.getInstance().updateMemberRecent(messageVo.getToNo(), true);
        messageVo = DBManager.getInstance().saveMessage(messageVo);
        NoticeUtil.getInstance().playNotice();
        EventBus.getDefault().post(new NoticeAddMemberEvent(messageVo));
    }

    @Override
    public void getSingleMemberInfo(final long partyNo, final long memberNo, final String time, final boolean isLocal) {
        ApiAction.getInstance().getContactInfo(new ContactAddInfoReq(memberNo), new ApiStringCallbackListener() {
            @Override
            public void onSuccess(String data) {
                ApiResponse<UserDto> result = GsonUtil.getInstance().fromJson(data, new TypeToken<ApiResponse<UserDto>>() {
                }.getType());
                if(result.isLegal()){
                    DBManager.getInstance().saveUser(result.getData());
                    DBManager.getInstance().saveMember(EntityUtil.getNewMember(
                            partyNo,
                            result.getData().getUserNo(),
                            ConstantUtil.MEMBER_MEMBER,
                            result.getData().getNickName()));
                    if(isLocal){
                        EventBus.getDefault().post(new UpdateContactEvent());
                        noticeClientNewMember(partyNo, memberNo, time);
                    }else{
                        noticeServiceNewMember(partyNo, memberNo, time);
                    }
                }
            }

            @Override
            public void onFailure(int errorCode) {

            }
        });
    }

    @Override
    public void getPartyAdd(PartyAddReq partyAddReq, String path, final ActionFileCallbackListener<ActionResponse<Void>> actionFileCallbackListener) {
        ApiAction.getInstance().getPartyAdd(partyAddReq, path, new ApiFileCallbackListener() {
            @Override
            public void onSuccess(final String data) {
                Tasks.executeInBackground(context, new BackgroundWork<ActionResponse<Void>>() {
                    @Override
                    public ActionResponse<Void> doInBackground() throws Exception {
                        ApiResponse<ContactDto> result = GsonUtil.getInstance().fromJson(data, new TypeToken<ApiResponse<ContactDto>>(){}.getType());
                        if(result.isLegal() && result.getData().getType() == ConstantUtil.CONTACT_PARTY){
                            DBManager.getInstance().saveParty(EntityUtil.getPartyFromContactDto(result.getData()));
                            DBManager.getInstance().saveMemberList(getMemberListFromContactDto(result.getData()));
                            EventBus.getDefault().post(new UpdateContactEvent());
                        }
                        return ActionResponse.getActionRespon(null);
                    }
                }, new Completion<ActionResponse<Void>>() {
                    @Override
                    public void onSuccess(Context context, ActionResponse<Void> result) {
                        actionFileCallbackListener.onSuccess(result);
                    }

                    @Override
                    public void onError(Context context, Exception e) {
                        actionFileCallbackListener.onSuccess(ActionResponse.<Void>getActionResponError());
                    }
                });
            }

            @Override
            public void onFailure(int errorCode) {
                actionFileCallbackListener.onFailure(errorCode);
            }

            @Override
            public void onProgress(float progress) {
                actionFileCallbackListener.onProgress(progress);
            }
        });
    }

    @Override
    public void getFriendDelete(final FriendDeleteReq friendDeleteReq, final ActionStringCallbackListener<ActionResponse<Void>> actionStringCallbackListener) {
        ApiAction.getInstance().getFriendDelete(friendDeleteReq, new ApiStringCallbackListener() {
            @Override
            public void onSuccess(final String data) {
                Tasks.executeInBackground(context, new BackgroundWork<ActionResponse<Void>>() {
                    @Override
                    public ActionResponse<Void> doInBackground() throws Exception {
                        ApiResponse<Void> result = GsonUtil.getInstance().fromJson(data, new TypeToken<ApiResponse<Void>>(){}.getType());
                        if(result.isLegal()){
                            DBManager.getInstance().deleteFriend(friendDeleteReq.getFriendNo());
                            ChattingTemper.getInstance().deleteChattingVo(friendDeleteReq.getFriendNo(), ConstantUtil.CHAT_FRI);
                            EventBus.getDefault().post(new DeleteContactEvent(ConstantUtil.DELETE_CONTACT_FRIEND, friendDeleteReq.getFriendNo()));
                        }
                        return ActionResponse.getActionRespon(null);
                    }
                }, new Completion<ActionResponse<Void>>() {
                    @Override
                    public void onSuccess(Context context, ActionResponse<Void> result) {
                        actionStringCallbackListener.onSuccess(result);
                    }

                    @Override
                    public void onError(Context context, Exception e) {
                        actionStringCallbackListener.onSuccess(ActionResponse.<Void>getActionResponError());
                    }
                });
            }

            @Override
            public void onFailure(int errorCode) {
                actionStringCallbackListener.onFailure(errorCode);
            }
        });
    }

    @Override
    public void getFriendRemarkUpdate(final RemarkUpdateReq remarkUpdateReq, final ActionStringCallbackListener<ActionResponse<Void>> actionStringCallbackListener) {
        ApiAction.getInstance().getfriendRemarkUpdate(remarkUpdateReq, new ApiStringCallbackListener() {
            @Override
            public void onSuccess(final String data) {
                Tasks.executeInBackground(context, new BackgroundWork<ActionResponse<Void>>() {
                    @Override
                    public ActionResponse<Void> doInBackground() throws Exception {
                        ApiResponse<String> result = GsonUtil.getInstance().fromJson(data, new TypeToken<ApiResponse<String>>(){}.getType());
                        if(result.isLegal()){
                            ContactTemper.getInstance().updateFriendRemark(remarkUpdateReq.getFriendNo(), result.getData());
                            DBManager.getInstance().updateFriendRemark(remarkUpdateReq.getFriendNo(), result.getData());
                            EventBus.getDefault().post(new UpdateRemarkEvent(remarkUpdateReq.getFriendNo(), result.getData()));
                        }
                        return ActionResponse.getActionRespon(null);
                    }
                }, new Completion<ActionResponse<Void>>() {
                    @Override
                    public void onSuccess(Context context, ActionResponse<Void> result) {
                        actionStringCallbackListener.onSuccess(result);
                    }

                    @Override
                    public void onError(Context context, Exception e) {
                        actionStringCallbackListener.onSuccess(ActionResponse.<Void>getActionResponError());
                    }
                });
            }

            @Override
            public void onFailure(int errorCode) {
                actionStringCallbackListener.onFailure(errorCode);
            }
        });
    }

    @Override
    public void getAddContact(final long userNo, final int type, final boolean isLocal) {
        ApiAction.getInstance().getContactInfo(new ContactAddInfoReq(userNo), new ApiStringCallbackListener() {
            @Override
            public void onSuccess(final String data) {
                Tasks.executeInBackground(context, new BackgroundWork<Void>() {
                    @Override
                    public Void doInBackground() throws Exception {
                        ApiResponse<UserDto> result = GsonUtil.getInstance().fromJson(data, new TypeToken<ApiResponse<UserDto>>() {
                        }.getType());
                        if(result.isLegal() && type != ConstantUtil.CONTACT_PARTY_APPLY_INFO) {
                            DBManager.getInstance().saveFriend(EntityUtil.getFriend(
                                    CustomSessionPreference.getInstance().getCustomSession().getUserNo(),
                                    result.getData().getUserNo(),
                                    result.getData().getNickName(),
                                    true
                            ));
                            DBManager.getInstance().saveUser(result.getData());
                        }
                        return null;
                    }
                }, new Completion<Void>() {
                    @Override
                    public void onSuccess(Context context, Void result) {
                        if(isLocal && type == ConstantUtil.CONTACT_FRIEND_APPLY_INFO_BEFORE){
                            EventBus.getDefault().post(new RequestContactEvent());
                        }else if(isLocal && type == ConstantUtil.CONTACT_FRIEND_APPLY_INFO_AFTER){
                            EventBus.getDefault().post(new UpdateContactEvent());
                        }else if(!isLocal && type == ConstantUtil.CONTACT_FRIEND_APPLY_INFO_BEFORE){
                            MessageServiceHandler.getInstance().sendMessageToClient(ConstantUtil.HANDLER_FRIEND_APPLY, null);
                        }else if(!isLocal && type == ConstantUtil.CONTACT_FRIEND_APPLY_INFO_AFTER){
                            MessageServiceHandler.getInstance().sendMessageToClient(ConstantUtil.HANDLER_FRIEND_APPLY_AGREE, null);
                        }else if(!isLocal && type == ConstantUtil.CONTACT_PARTY_APPLY_INFO){
                            MessageServiceHandler.getInstance().sendMessageToClient(ConstantUtil.HANDLER_PARTY_APPLY, null);
                        }
                    }

                    @Override
                    public void onError(Context context, Exception e) {

                    }
                });
            }

            @Override
            public void onFailure(int errorCode) {
            }
        });
    }

    @Override
    public void getContactsSearch(ContactsSearchReq contactsSearchReq, final ActionStringCallbackListener<ActionResponse<List<ContactDto>>> actionStringCallbackListener) {
        ApiAction.getInstance().getContactsSearch(contactsSearchReq, new ApiStringCallbackListener() {
            @Override
            public void onSuccess(final String data) {
                Tasks.executeInBackground(context, new BackgroundWork<ActionResponse<List<ContactDto>>>() {
                    @Override
                    public ActionResponse<List<ContactDto>> doInBackground() throws Exception {
                        ApiResponse<List<ContactDto>> result = GsonUtil.getInstance().fromJson(data, new TypeToken<ApiResponse<List<ContactDto>>>(){}.getType());
                        return ActionResponse.getActionResponFromApiRespon(result);
                    }
                }, new Completion<ActionResponse<List<ContactDto>>>() {
                    @Override
                    public void onSuccess(Context context, ActionResponse<List<ContactDto>> result) {
                        actionStringCallbackListener.onSuccess(result);
                    }

                    @Override
                    public void onError(Context context, Exception e) {
                        actionStringCallbackListener.onSuccess(ActionResponse.<List<ContactDto>>getActionResponError());
                    }
                });
            }

            @Override
            public void onFailure(int errorCode) {
                actionStringCallbackListener.onFailure(errorCode);
            }
        });
    }

    @Override
    public void getPartyNameUpdate(final PartyNameUpdateReq partyNameUpdateReq, final ActionStringCallbackListener<ActionResponse<String>> actionStringCallbackListener) {
        ApiAction.getInstance().getPartyNameUpdate(partyNameUpdateReq, new ApiStringCallbackListener() {
            @Override
            public void onSuccess(final String data) {
                Tasks.executeInBackground(context, new BackgroundWork<ActionResponse<String>>() {
                    @Override
                    public ActionResponse<String> doInBackground() throws Exception {
                        ApiResponse<String> result = GsonUtil.getInstance().fromJson(data, new TypeToken<ApiResponse<String>>(){}.getType());
                        if(result.isLegal()){
                            ContactTemper.getInstance().updatePartyName(partyNameUpdateReq.getPartyNo(), result.getData());
                            DBManager.getInstance().updatePartyName(partyNameUpdateReq.getPartyNo(), result.getData());
                            EventBus.getDefault().post(new UpdatePartyEvent(partyNameUpdateReq.getPartyNo(), result.getData(), ConstantUtil.CHAT_PARTY_NAME));
                        }
                        return ActionResponse.getActionResponFromApiRespon(result);
                    }
                }, new Completion<ActionResponse<String>>() {
                    @Override
                    public void onSuccess(Context context, ActionResponse<String> result) {
                        actionStringCallbackListener.onSuccess(result);
                    }

                    @Override
                    public void onError(Context context, Exception e) {
                        actionStringCallbackListener.onSuccess(ActionResponse.<String>getActionResponError());
                    }
                });
            }

            @Override
            public void onFailure(int errorCode) {
                actionStringCallbackListener.onFailure(errorCode);
            }
        });
    }

    @Override
    public void getPartyIntroduceUpdate(final PartyIntroduceUpdateReq partyIntroduceUpdateReq, final ActionStringCallbackListener<ActionResponse<String>> actionStringCallbackListener) {
        ApiAction.getInstance().getPartyIntroduceUpdate(partyIntroduceUpdateReq, new ApiStringCallbackListener() {
            @Override
            public void onSuccess(final String data) {
                Tasks.executeInBackground(context, new BackgroundWork<ActionResponse<String>>() {
                    @Override
                    public ActionResponse<String> doInBackground() throws Exception {
                        ApiResponse<String> result = GsonUtil.getInstance().fromJson(data, new TypeToken<ApiResponse<String>>(){}.getType());
                        if(result.isLegal()){
                            ContactTemper.getInstance().updatePartyIntroduce(partyIntroduceUpdateReq.getPartyNo(), result.getData());
                            DBManager.getInstance().updatePartyIntroduce(partyIntroduceUpdateReq.getPartyNo(), result.getData());
                            EventBus.getDefault().post(new UpdatePartyEvent(partyIntroduceUpdateReq.getPartyNo(), result.getData(), ConstantUtil.CHAT_PARTY_INTRODUCE));
                        }
                        return ActionResponse.getActionResponFromApiRespon(result);
                    }
                }, new Completion<ActionResponse<String>>() {
                    @Override
                    public void onSuccess(Context context, ActionResponse<String> result) {
                        actionStringCallbackListener.onSuccess(result);
                    }

                    @Override
                    public void onError(Context context, Exception e) {
                        actionStringCallbackListener.onSuccess(ActionResponse.<String>getActionResponError());
                    }
                });
            }

            @Override
            public void onFailure(int errorCode) {
                actionStringCallbackListener.onFailure(errorCode);
            }
        });
    }

    @Override
    public void getMemberNameUpdate(final MemberNameUpdateReq memberNameUpdateReq, final ActionStringCallbackListener<ActionResponse<String>> actionStringCallbackListener) {
        ApiAction.getInstance().getMemberNameUpdate(memberNameUpdateReq, new ApiStringCallbackListener() {
            @Override
            public void onSuccess(final String data) {
                Tasks.executeInBackground(context, new BackgroundWork<ActionResponse<String>>() {
                    @Override
                    public ActionResponse<String> doInBackground() throws Exception {
                        ApiResponse<String> result = GsonUtil.getInstance().fromJson(data, new TypeToken<ApiResponse<String>>(){}.getType());
                        if(result.isLegal()){
                            ContactTemper.getInstance().updateMemberName(memberNameUpdateReq.getPartyNo(), CustomSessionPreference.getInstance().getCustomSession().getUserNo(), result.getData());
                            DBManager.getInstance().updateMemberName(memberNameUpdateReq.getPartyNo(),CustomSessionPreference.getInstance().getCustomSession().getUserNo(), result.getData());
                            EventBus.getDefault().post(new UpdatePartyEvent(memberNameUpdateReq.getPartyNo(),result.getData(), ConstantUtil.CHAT_PARTY_MEMBER_NAME));
                        }
                        return ActionResponse.getActionResponFromApiRespon(result);
                    }
                }, new Completion<ActionResponse<String>>() {
                    @Override
                    public void onSuccess(Context context, ActionResponse<String> result) {
                        actionStringCallbackListener.onSuccess(result);
                    }

                    @Override
                    public void onError(Context context, Exception e) {
                        actionStringCallbackListener.onSuccess(ActionResponse.<String>getActionResponError());
                    }
                });
            }

            @Override
            public void onFailure(int errorCode) {
                actionStringCallbackListener.onFailure(errorCode);
            }
        });
    }

    @Override
    public void getContactFromApi(final ContactAddInfoReq contactAddInfoReq, final ActionStringCallbackListener<ActionResponse<FriendVo>> actionStringCallbackListener) {
        ApiAction.getInstance().getContactInfo(contactAddInfoReq, new ApiStringCallbackListener() {
            @Override
            public void onSuccess(final String data) {
                Tasks.executeInBackground(context, new BackgroundWork<ActionResponse<FriendVo>>() {
                    @Override
                    public ActionResponse<FriendVo> doInBackground() throws Exception {
                        ApiResponse<UserDto> result = GsonUtil.getInstance().fromJson(data, new TypeToken<ApiResponse<UserDto>>() {
                        }.getType());
                        if(result.isLegal()){
                            DBManager.getInstance().saveUser(result.getData());
                            return ActionResponse.getActionRespon(result.getMessage(), result.getRetCode(), DBManager.getInstance().getFriendVo(contactAddInfoReq.getUserNo()));
                        }
                        return ActionResponse.getActionRespon(result.getMessage(), result.getRetCode(), (FriendVo)null);
                    }
                }, new Completion<ActionResponse<FriendVo>>() {
                    @Override
                    public void onSuccess(Context context, ActionResponse<FriendVo> result) {
                        actionStringCallbackListener.onSuccess(result);
                    }
                    @Override
                    public void onError(Context context, Exception e) {
                        actionStringCallbackListener.onSuccess(ActionResponse.<FriendVo>getActionResponError());
                    }
                });

            }

            @Override
            public void onFailure(int errorCode) {
                actionStringCallbackListener.onFailure(errorCode);
            }
        });
    }

    @Override
    public void getContactFromLocal(final long userNo,  final ActionStringCallbackListener<ActionResponse<FriendVo>> actionStringCallbackListener) {
        FriendVo friendVo = ContactTemper.getInstance().getFriendVo(userNo);
        if(friendVo == null){
            Tasks.executeInBackground(context, new BackgroundWork<ActionResponse<FriendVo>>() {
                @Override
                public ActionResponse<FriendVo> doInBackground() throws Exception {
                    FriendVo f = DBManager.getInstance().getFriendVo(userNo);
                    return ActionResponse.getActionRespon(f);
                }
            }, new Completion<ActionResponse<FriendVo>>() {
                @Override
                public void onSuccess(Context context, ActionResponse<FriendVo> result) {
                    actionStringCallbackListener.onSuccess(result);
                }

                @Override
                public void onError(Context context, Exception e) {
                    actionStringCallbackListener.onSuccess(ActionResponse.<FriendVo>getActionResponError());
                }
            });
        }else{
            actionStringCallbackListener.onSuccess(ActionResponse.getActionRespon(friendVo));
        }
    }

    @Override
    public List<PartyVo> getPartyVoList(){
        List<PartyVo> partyVoList = DBManager.getInstance().getPartyVoList();
        Collections.sort(partyVoList, new ContactComparator());
        if(partyVoList.size() == 1){
            char capital = PinyinUtil.getHeadCapitalByChar(partyVoList.get(0).getDisplayName().charAt(0));
            partyVoList.get(0).setSortLetter((capital >= 'A' && capital <= 'Z') ? (capital + "") : "#");
        }
        return partyVoList;
    }

    @Override
    public void getContactList(final boolean addToContactTemper, final ActionStringCallbackListener<ActionResponse<List<FriendVo>>> actionStringCallbackListener){
        //先从本地加载并显示
        Tasks.executeInBackground(context, new BackgroundWork<ActionResponse<List<FriendVo>>>() {
            @Override
            public ActionResponse<List<FriendVo>> doInBackground() throws Exception {
                return ActionResponse.getActionRespon(getFriendVoListFromDB(addToContactTemper));
            }
        }, new Completion<ActionResponse<List<FriendVo>>>() {
            @Override
            public void onSuccess(Context context, ActionResponse<List<FriendVo>> result) {
                actionStringCallbackListener.onSuccess(result);
                String updateTime = ACache.getInstance().getAsString(ConstantUtil.ACACHE_LAST_TIME_CONTACT);
                if(StringUtil.isEmpty(updateTime) || TimeUtil.getHourDifferenceBetweenTime(updateTime) >= 1){
                    getContactListFromApi(actionStringCallbackListener);
                }
            }

            @Override
            public void onError(Context context, Exception e) {
                actionStringCallbackListener.onSuccess(ActionResponse.<List<FriendVo>>getActionResponError());
            }
        });
    }

    private void getContactListFromApi(final ActionStringCallbackListener<ActionResponse<List<FriendVo>>> actionStringCallbackListener){
        ApiAction.getInstance().getContactList(new ApiStringCallbackListener() {
            @Override
            public void onSuccess(final String data) {
                Tasks.executeInBackground(context, new BackgroundWork<ActionResponse<List<FriendVo>>>() {
                    @Override
                    public ActionResponse<List<FriendVo>> doInBackground() throws Exception {
                        ApiResponse<List<ContactDto>> result = GsonUtil.getInstance().fromJson(data, new TypeToken<ApiResponse<List<ContactDto>>>() {
                        }.getType());
                        if(result.isLegal()){
                            saveContactsFromList(result.getData());
                            return ActionResponse.getActionRespon(result.getMessage(), result.getRetCode(), getFriendVoListFromDB(true));
                        }else{
                            return ActionResponse.getActionRespon(result.getMessage(), result.getRetCode(), (List<FriendVo>)null);
                        }
                    }
                }, new Completion<ActionResponse<List<FriendVo>>>() {
                    @Override
                    public void onSuccess(Context context, ActionResponse<List<FriendVo>> result) {
                        ACache.getInstance().put(ConstantUtil.ACACHE_LAST_TIME_CONTACT, TimeUtil.getCurrentTimeYYMMDD_HHMMSS());
                        actionStringCallbackListener.onSuccess(result);
                    }

                    @Override
                    public void onError(Context context, Exception e) {
                        actionStringCallbackListener.onSuccess(ActionResponse.<List<FriendVo>>getActionResponError());
                    }
                });
            }

            @Override
            public void onFailure(int errorCode) {
                actionStringCallbackListener.onFailure(errorCode);
            }
        });
    }

    private List<FriendVo> getFriendVoListFromDB(boolean addToContactTemper){
        List<FriendVo> friendVoList = DBManager.getInstance().getFriendVoList(addToContactTemper);
        DBManager.getInstance().getPartyVoList();
        Collections.sort(friendVoList, new ContactComparator());
        if(friendVoList.size() == 1){
            char capital = PinyinUtil.getHeadCapitalByChar(friendVoList.get(0).getDisplayName().charAt(0));
            friendVoList.get(0).setSortLetter((capital >= 'A' && capital <= 'Z') ? (capital + "") : "#");
        }

        return friendVoList;
    }

    private void saveContactsFromList(List<ContactDto> contactDtoList){
        List<Friend> friendList = new ArrayList<>();
        List<Party> partyList = new ArrayList<>();
        List<User> userList = new ArrayList<>();
        List<Member> memberList = new ArrayList<>();

        for(ContactDto contactDto : contactDtoList){
            if(contactDto.getType() == ConstantUtil.CONTACT_FRIEND){
                friendList.add(EntityUtil.getFriendFromContactDto(contactDto, CustomSessionPreference.getInstance().getCustomSession().getUserNo()));
                userList.add(EntityUtil.getUserFromContactDto(contactDto));
            }else if(contactDto.getType() == ConstantUtil.CONTACT_PARTY){
                partyList.add(EntityUtil.getPartyFromContactDto(contactDto));
                memberList.addAll(getMemberListFromContactDto(contactDto));
            }
        }
        DBManager.getInstance().saveUserList(userList);
        DBManager.getInstance().saveFriendList(friendList);
        DBManager.getInstance().savePartyList(partyList);
        DBManager.getInstance().saveMemberList(memberList);
    }

    private List<Member> getMemberListFromContactDto(ContactDto contactDto){
        List<Member> memberList = new ArrayList<>();
        List<MemberDto> memberDtoList = new ArrayList<>();
        List<User> userList = new ArrayList<>();

        memberDtoList.addAll(contactDto.getMemberList());
        for(MemberDto memberDto : memberDtoList){
            Member member = EntityUtil.getMemberFromMemberDto(memberDto);
            User user = EntityUtil.getUserFromMemberDto(memberDto);

            userList.add(user);
            memberList.add(member);
        }
        DBManager.getInstance().saveUserListSelective(userList);

        return memberList;
    }

}
