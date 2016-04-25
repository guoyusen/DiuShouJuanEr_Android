package com.bili.diushoujuaner.model.actionhelper.action;

import android.content.Context;

import com.bili.diushoujuaner.model.actionhelper.IContactAction;
import com.bili.diushoujuaner.model.actionhelper.respon.ActionRespon;
import com.bili.diushoujuaner.model.apihelper.ApiRespon;
import com.bili.diushoujuaner.model.apihelper.api.ApiAction;
import com.bili.diushoujuaner.model.apihelper.callback.ApiFileCallbackListener;
import com.bili.diushoujuaner.model.apihelper.callback.ApiStringCallbackListener;
import com.bili.diushoujuaner.model.apihelper.request.ContactInfoReq;
import com.bili.diushoujuaner.model.apihelper.request.ContactsSearchReq;
import com.bili.diushoujuaner.model.apihelper.request.FriendDeleteReq;
import com.bili.diushoujuaner.model.apihelper.request.MemberNameUpdateReq;
import com.bili.diushoujuaner.model.apihelper.request.PartyAddReq;
import com.bili.diushoujuaner.model.apihelper.request.PartyIntroduceUpdateReq;
import com.bili.diushoujuaner.model.apihelper.request.PartyNameUpdateReq;
import com.bili.diushoujuaner.model.apihelper.request.RemarkUpdateReq;
import com.bili.diushoujuaner.model.callback.ActionFileCallbackListener;
import com.bili.diushoujuaner.model.eventhelper.RequestContactEvent;
import com.bili.diushoujuaner.model.eventhelper.AddContactEvent;
import com.bili.diushoujuaner.model.eventhelper.UpdateRemarkEvent;
import com.bili.diushoujuaner.model.tempHelper.ChattingTemper;
import com.bili.diushoujuaner.utils.ConstantUtil;
import com.bili.diushoujuaner.utils.EntityUtil;
import com.bili.diushoujuaner.utils.StringUtil;
import com.bili.diushoujuaner.utils.TimeUtil;
import com.bili.diushoujuaner.utils.entity.dto.UserDto;
import com.bili.diushoujuaner.model.cachehelper.ACache;
import com.bili.diushoujuaner.model.callback.ActionStringCallbackListener;
import com.bili.diushoujuaner.model.databasehelper.DBManager;
import com.bili.diushoujuaner.utils.entity.po.Friend;
import com.bili.diushoujuaner.utils.entity.po.Member;
import com.bili.diushoujuaner.utils.entity.po.Party;
import com.bili.diushoujuaner.utils.entity.po.User;
import com.bili.diushoujuaner.model.preferhelper.CustomSessionPreference;
import com.bili.diushoujuaner.model.tempHelper.ContactTemper;
import com.bili.diushoujuaner.utils.GsonUtil;
import com.bili.diushoujuaner.utils.entity.vo.FriendVo;
import com.bili.diushoujuaner.utils.entity.vo.PartyVo;
import com.bili.diushoujuaner.utils.comparator.ContactComparator;
import com.bili.diushoujuaner.utils.PinyinUtil;
import com.bili.diushoujuaner.utils.entity.dto.ContactDto;
import com.bili.diushoujuaner.utils.entity.dto.MemberDto;
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
    public void getPartyAdd(PartyAddReq partyAddReq, String path, final ActionFileCallbackListener<ActionRespon<Void>> actionFileCallbackListener) {
        ApiAction.getInstance().getPartyAdd(partyAddReq, path, new ApiFileCallbackListener() {
            @Override
            public void onSuccess(final String data) {
                Tasks.executeInBackground(context, new BackgroundWork<ActionRespon<Void>>() {
                    @Override
                    public ActionRespon<Void> doInBackground() throws Exception {
                        ApiRespon<ContactDto> result = GsonUtil.getInstance().fromJson(data, new TypeToken<ApiRespon<ContactDto>>(){}.getType());
                        if(result.getIsLegal() && result.getData().getType() == ConstantUtil.CONTACT_PARTY){
                            DBManager.getInstance().saveParty(EntityUtil.getPartyFromContactDto(result.getData()));
                            DBManager.getInstance().saveMemberList(getMemberListFromContactDto(result.getData()));
                            EventBus.getDefault().post(new AddContactEvent());
                        }
                        return ActionRespon.getActionRespon(null);
                    }
                }, new Completion<ActionRespon<Void>>() {
                    @Override
                    public void onSuccess(Context context, ActionRespon<Void> result) {
                        actionFileCallbackListener.onSuccess(result);
                    }

                    @Override
                    public void onError(Context context, Exception e) {
                        actionFileCallbackListener.onSuccess(ActionRespon.<Void>getActionResponError());
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
    public void getFriendDelete(final FriendDeleteReq friendDeleteReq, final ActionStringCallbackListener<ActionRespon<Void>> actionStringCallbackListener) {
        ApiAction.getInstance().getFriendDelete(friendDeleteReq, new ApiStringCallbackListener() {
            @Override
            public void onSuccess(final String data) {
                Tasks.executeInBackground(context, new BackgroundWork<ActionRespon<Void>>() {
                    @Override
                    public ActionRespon<Void> doInBackground() throws Exception {
                        ApiRespon<Void> result = GsonUtil.getInstance().fromJson(data, new TypeToken<ApiRespon<Void>>(){}.getType());
                        if(result.getIsLegal()){
                            DBManager.getInstance().deleteFriend(friendDeleteReq.getFriendNo());
                            ChattingTemper.getInstance().deleteChattingVo(friendDeleteReq.getFriendNo(), ConstantUtil.CHAT_FRI);
                        }
                        return ActionRespon.getActionRespon(null);
                    }
                }, new Completion<ActionRespon<Void>>() {
                    @Override
                    public void onSuccess(Context context, ActionRespon<Void> result) {
                        actionStringCallbackListener.onSuccess(result);
                    }

                    @Override
                    public void onError(Context context, Exception e) {
                        actionStringCallbackListener.onSuccess(ActionRespon.<Void>getActionResponError());
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
    public void getFriendRemarkUpdate(final RemarkUpdateReq remarkUpdateReq, final ActionStringCallbackListener<ActionRespon<Void>> actionStringCallbackListener) {
        ApiAction.getInstance().getfriendRemarkUpdate(remarkUpdateReq, new ApiStringCallbackListener() {
            @Override
            public void onSuccess(final String data) {
                Tasks.executeInBackground(context, new BackgroundWork<ActionRespon<Void>>() {
                    @Override
                    public ActionRespon<Void> doInBackground() throws Exception {
                        ApiRespon<String> result = GsonUtil.getInstance().fromJson(data, new TypeToken<ApiRespon<String>>(){}.getType());
                        if(result.getIsLegal()){
                            ContactTemper.getInstance().updateFriendRemark(remarkUpdateReq.getFriendNo(), result.getData());
                            DBManager.getInstance().updateFriendRemark(remarkUpdateReq.getFriendNo(), result.getData());
                            EventBus.getDefault().post(new UpdateRemarkEvent(remarkUpdateReq.getFriendNo(), result.getData()));
                        }
                        return ActionRespon.getActionRespon(null);
                    }
                }, new Completion<ActionRespon<Void>>() {
                    @Override
                    public void onSuccess(Context context, ActionRespon<Void> result) {
                        actionStringCallbackListener.onSuccess(result);
                    }

                    @Override
                    public void onError(Context context, Exception e) {
                        actionStringCallbackListener.onSuccess(ActionRespon.<Void>getActionResponError());
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
    public void getAddContact(final long userNo, final int type) {
        User user = DBManager.getInstance().getUser(userNo);
        if(user == null){
            ContactInfoReq contactInfoReq = new ContactInfoReq();
            contactInfoReq.setUserNo(userNo);
            ApiAction.getInstance().getContactInfo(contactInfoReq, new ApiStringCallbackListener() {
                @Override
                public void onSuccess(final String data) {
                    Tasks.executeInBackground(context, new BackgroundWork<Void>() {
                        @Override
                        public Void doInBackground() throws Exception {
                            ApiRespon<UserDto> result = GsonUtil.getInstance().fromJson(data, new TypeToken<ApiRespon<UserDto>>() {
                            }.getType());
                            if(result.getIsLegal()) {
                                Friend friend = new Friend();
                                friend.setRemark(result.getData().getNickName());
                                friend.setFriendNo(result.getData().getUserNo());
                                friend.setOwnerNo(CustomSessionPreference.getInstance().getCustomSession().getUserNo());
                                friend.setRecent(true);
                                DBManager.getInstance().saveFriend(friend);
                                DBManager.getInstance().saveUser(result.getData());
                            }
                            return null;
                        }
                    }, new Completion<Void>() {
                        @Override
                        public void onSuccess(Context context, Void result) {
                            if(type == ConstantUtil.CONTACT_INFO_ADD_BEFORE){
                                EventBus.getDefault().post(new RequestContactEvent());
                            }else if(type == ConstantUtil.CONTACT_INFO_ADD_AFTER) {
                                //从本地更新联系人信息
                                EventBus.getDefault().post(new AddContactEvent());
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
        }else{
            if(type == ConstantUtil.CONTACT_INFO_ADD_BEFORE){
                EventBus.getDefault().post(new RequestContactEvent());
            }else if(type == ConstantUtil.CONTACT_INFO_ADD_AFTER) {
                //从本地更新联系人信息
                Friend friend = new Friend();
                friend.setRemark(user.getNickName());
                friend.setFriendNo(user.getUserNo());
                friend.setOwnerNo(CustomSessionPreference.getInstance().getCustomSession().getUserNo());
                friend.setRecent(true);
                DBManager.getInstance().saveFriend(friend);
                EventBus.getDefault().post(new AddContactEvent());
            }
        }
    }

    @Override
    public void getContactsSearch(ContactsSearchReq contactsSearchReq, final ActionStringCallbackListener<ActionRespon<List<ContactDto>>> actionStringCallbackListener) {
        ApiAction.getInstance().getContactsSearch(contactsSearchReq, new ApiStringCallbackListener() {
            @Override
            public void onSuccess(final String data) {
                Tasks.executeInBackground(context, new BackgroundWork<ActionRespon<List<ContactDto>>>() {
                    @Override
                    public ActionRespon<List<ContactDto>> doInBackground() throws Exception {
                        ApiRespon<List<ContactDto>> result = GsonUtil.getInstance().fromJson(data, new TypeToken<ApiRespon<List<ContactDto>>>(){}.getType());
                        return ActionRespon.getActionResponFromApiRespon(result);
                    }
                }, new Completion<ActionRespon<List<ContactDto>>>() {
                    @Override
                    public void onSuccess(Context context, ActionRespon<List<ContactDto>> result) {
                        actionStringCallbackListener.onSuccess(result);
                    }

                    @Override
                    public void onError(Context context, Exception e) {
                        actionStringCallbackListener.onSuccess(ActionRespon.<List<ContactDto>>getActionResponError());
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
    public void getPartyNameUpdate(final PartyNameUpdateReq partyNameUpdateReq, final ActionStringCallbackListener<ActionRespon<String>> actionStringCallbackListener) {
        ApiAction.getInstance().getPartyNameUpdate(partyNameUpdateReq, new ApiStringCallbackListener() {
            @Override
            public void onSuccess(final String data) {
                Tasks.executeInBackground(context, new BackgroundWork<ActionRespon<String>>() {
                    @Override
                    public ActionRespon<String> doInBackground() throws Exception {
                        ApiRespon<String> result = GsonUtil.getInstance().fromJson(data, new TypeToken<ApiRespon<String>>(){}.getType());
                        if(result.getIsLegal()){
                            ContactTemper.getInstance().updatePartyName(partyNameUpdateReq.getPartyNo(), result.getData());
                            DBManager.getInstance().updatePartyName(partyNameUpdateReq.getPartyNo(), result.getData());
                        }
                        return ActionRespon.getActionResponFromApiRespon(result);
                    }
                }, new Completion<ActionRespon<String>>() {
                    @Override
                    public void onSuccess(Context context, ActionRespon<String> result) {
                        actionStringCallbackListener.onSuccess(result);
                    }

                    @Override
                    public void onError(Context context, Exception e) {
                        actionStringCallbackListener.onSuccess(ActionRespon.<String>getActionResponError());
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
    public void getPartyIntroduceUpdate(final PartyIntroduceUpdateReq partyIntroduceUpdateReq, final ActionStringCallbackListener<ActionRespon<String>> actionStringCallbackListener) {
        ApiAction.getInstance().getPartyIntroduceUpdate(partyIntroduceUpdateReq, new ApiStringCallbackListener() {
            @Override
            public void onSuccess(final String data) {
                Tasks.executeInBackground(context, new BackgroundWork<ActionRespon<String>>() {
                    @Override
                    public ActionRespon<String> doInBackground() throws Exception {
                        ApiRespon<String> result = GsonUtil.getInstance().fromJson(data, new TypeToken<ApiRespon<String>>(){}.getType());
                        if(result.getIsLegal()){
                            ContactTemper.getInstance().updatePartyIntroduce(partyIntroduceUpdateReq.getPartyNo(), result.getData());
                            DBManager.getInstance().updatePartyIntroduce(partyIntroduceUpdateReq.getPartyNo(), result.getData());
                        }
                        return ActionRespon.getActionResponFromApiRespon(result);
                    }
                }, new Completion<ActionRespon<String>>() {
                    @Override
                    public void onSuccess(Context context, ActionRespon<String> result) {
                        actionStringCallbackListener.onSuccess(result);
                    }

                    @Override
                    public void onError(Context context, Exception e) {
                        actionStringCallbackListener.onSuccess(ActionRespon.<String>getActionResponError());
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
    public void getMemberNameUpdate(final MemberNameUpdateReq memberNameUpdateReq, final ActionStringCallbackListener<ActionRespon<String>> actionStringCallbackListener) {
        ApiAction.getInstance().getMemberNameUpdate(memberNameUpdateReq, new ApiStringCallbackListener() {
            @Override
            public void onSuccess(final String data) {
                Tasks.executeInBackground(context, new BackgroundWork<ActionRespon<String>>() {
                    @Override
                    public ActionRespon<String> doInBackground() throws Exception {
                        ApiRespon<String> result = GsonUtil.getInstance().fromJson(data, new TypeToken<ApiRespon<String>>(){}.getType());
                        if(result.getIsLegal()){
                            ContactTemper.getInstance().updateMemberName(memberNameUpdateReq.getPartyNo(), CustomSessionPreference.getInstance().getCustomSession().getUserNo(), result.getData());
                            DBManager.getInstance().updateMemberName(memberNameUpdateReq.getPartyNo(),CustomSessionPreference.getInstance().getCustomSession().getUserNo(), result.getData());
                        }
                        return ActionRespon.getActionResponFromApiRespon(result);
                    }
                }, new Completion<ActionRespon<String>>() {
                    @Override
                    public void onSuccess(Context context, ActionRespon<String> result) {
                        actionStringCallbackListener.onSuccess(result);
                    }

                    @Override
                    public void onError(Context context, Exception e) {
                        actionStringCallbackListener.onSuccess(ActionRespon.<String>getActionResponError());
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
    public void getContactFromApi(final ContactInfoReq contactInfoReq, final ActionStringCallbackListener<ActionRespon<FriendVo>> actionStringCallbackListener) {
        ApiAction.getInstance().getContactInfo(contactInfoReq, new ApiStringCallbackListener() {
            @Override
            public void onSuccess(final String data) {
                Tasks.executeInBackground(context, new BackgroundWork<ActionRespon<FriendVo>>() {
                    @Override
                    public ActionRespon<FriendVo> doInBackground() throws Exception {
                        ApiRespon<UserDto> result = GsonUtil.getInstance().fromJson(data, new TypeToken<ApiRespon<UserDto>>() {
                        }.getType());
                        if(result.getIsLegal()){
                            DBManager.getInstance().saveUser(result.getData());
                            return ActionRespon.getActionRespon(result.getMessage(), result.getRetCode(), DBManager.getInstance().getFriendVo(contactInfoReq.getUserNo()));
                        }
                        return ActionRespon.getActionRespon(result.getMessage(), result.getRetCode(), (FriendVo)null);
                    }
                }, new Completion<ActionRespon<FriendVo>>() {
                    @Override
                    public void onSuccess(Context context, ActionRespon<FriendVo> result) {
                        actionStringCallbackListener.onSuccess(result);
                    }
                    @Override
                    public void onError(Context context, Exception e) {
                        actionStringCallbackListener.onSuccess(ActionRespon.<FriendVo>getActionResponError());
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
    public void getContactFromLocal(final long userNo,  final ActionStringCallbackListener<ActionRespon<FriendVo>> actionStringCallbackListener) {
        FriendVo friendVo = ContactTemper.getInstance().getFriendVo(userNo);
        if(friendVo == null){
            Tasks.executeInBackground(context, new BackgroundWork<ActionRespon<FriendVo>>() {
                @Override
                public ActionRespon<FriendVo> doInBackground() throws Exception {
                    FriendVo f = DBManager.getInstance().getFriendVo(userNo);
                    return ActionRespon.getActionRespon(f);
                }
            }, new Completion<ActionRespon<FriendVo>>() {
                @Override
                public void onSuccess(Context context, ActionRespon<FriendVo> result) {
                    actionStringCallbackListener.onSuccess(result);
                }

                @Override
                public void onError(Context context, Exception e) {
                    actionStringCallbackListener.onSuccess(ActionRespon.<FriendVo>getActionResponError());
                }
            });
        }else{
            actionStringCallbackListener.onSuccess(ActionRespon.getActionRespon(friendVo));
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
    public void getContactList(final ActionStringCallbackListener<ActionRespon<List<FriendVo>>> actionStringCallbackListener){
        //先从本地加载并显示
        Tasks.executeInBackground(context, new BackgroundWork<ActionRespon<List<FriendVo>>>() {
            @Override
            public ActionRespon<List<FriendVo>> doInBackground() throws Exception {
                return ActionRespon.getActionRespon(getFriendVoListFromDB());
            }
        }, new Completion<ActionRespon<List<FriendVo>>>() {
            @Override
            public void onSuccess(Context context, ActionRespon<List<FriendVo>> result) {
                actionStringCallbackListener.onSuccess(result);
                String updateTime = ACache.getInstance().getAsString(ConstantUtil.ACACHE_LAST_TIME_CONTACT);
                //TODO 完善后，更改全量获取联系人的时间间隔
                if(StringUtil.isEmpty(updateTime) || TimeUtil.getHourDifferenceBetweenTime(updateTime) > 1){
                    getContactListFromApi(actionStringCallbackListener);
                }
            }

            @Override
            public void onError(Context context, Exception e) {
                actionStringCallbackListener.onSuccess(ActionRespon.<List<FriendVo>>getActionResponError());
            }
        });
    }

    private void getContactListFromApi(final ActionStringCallbackListener<ActionRespon<List<FriendVo>>> actionStringCallbackListener){
        ApiAction.getInstance().getContactList(new ApiStringCallbackListener() {
            @Override
            public void onSuccess(final String data) {
                Tasks.executeInBackground(context, new BackgroundWork<ActionRespon<List<FriendVo>>>() {
                    @Override
                    public ActionRespon<List<FriendVo>> doInBackground() throws Exception {
                        ApiRespon<List<ContactDto>> result = GsonUtil.getInstance().fromJson(data, new TypeToken<ApiRespon<List<ContactDto>>>() {
                        }.getType());
                        if(result.getIsLegal()){
                            saveContactsFromList(result.getData());
                            return ActionRespon.getActionRespon(result.getMessage(), result.getRetCode(), getFriendVoListFromDB());
                        }else{
                            return ActionRespon.getActionRespon(result.getMessage(), result.getRetCode(), (List<FriendVo>)null);
                        }
                    }
                }, new Completion<ActionRespon<List<FriendVo>>>() {
                    @Override
                    public void onSuccess(Context context, ActionRespon<List<FriendVo>> result) {
                        ACache.getInstance().put(ConstantUtil.ACACHE_LAST_TIME_CONTACT, TimeUtil.getCurrentTimeYYMMDD_HHMMSS());
                        actionStringCallbackListener.onSuccess(result);
                    }

                    @Override
                    public void onError(Context context, Exception e) {
                        actionStringCallbackListener.onSuccess(ActionRespon.<List<FriendVo>>getActionResponError());
                    }
                });
            }

            @Override
            public void onFailure(int errorCode) {
                actionStringCallbackListener.onFailure(errorCode);
            }
        });
    }

    private List<FriendVo> getFriendVoListFromDB(){
        List<FriendVo> friendVoList = DBManager.getInstance().getFriendVoList();
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
