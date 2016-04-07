package com.bili.diushoujuaner.model.action.impl;

import android.content.Context;

import com.bili.diushoujuaner.model.action.IContactAction;
import com.bili.diushoujuaner.model.action.respon.ActionRespon;
import com.bili.diushoujuaner.model.apihelper.ApiRespon;
import com.bili.diushoujuaner.model.apihelper.api.ApiAction;
import com.bili.diushoujuaner.model.apihelper.callback.ApiCallbackListener;
import com.bili.diushoujuaner.model.apihelper.request.ContactInfoReq;
import com.bili.diushoujuaner.model.apihelper.response.UserRes;
import com.bili.diushoujuaner.model.cachehelper.ACache;
import com.bili.diushoujuaner.model.callback.ActionCallbackListener;
import com.bili.diushoujuaner.model.databasehelper.DBManager;
import com.bili.diushoujuaner.model.databasehelper.dao.Friend;
import com.bili.diushoujuaner.model.databasehelper.dao.Member;
import com.bili.diushoujuaner.model.databasehelper.dao.Party;
import com.bili.diushoujuaner.model.databasehelper.dao.User;
import com.bili.diushoujuaner.model.preferhelper.CustomSessionPreference;
import com.bili.diushoujuaner.model.tempHelper.ContactTemper;
import com.bili.diushoujuaner.utils.Common;
import com.bili.diushoujuaner.utils.Constant;
import com.bili.diushoujuaner.utils.GsonParser;
import com.bili.diushoujuaner.utils.entity.FriendVo;
import com.bili.diushoujuaner.utils.entity.PartyVo;
import com.bili.diushoujuaner.utils.pinyin.PinyinComparator;
import com.bili.diushoujuaner.utils.pinyin.PinyinUtil;
import com.bili.diushoujuaner.model.apihelper.response.ContactDto;
import com.bili.diushoujuaner.model.apihelper.response.MemberDto;
import com.google.gson.reflect.TypeToken;
import com.nanotasks.BackgroundWork;
import com.nanotasks.Completion;
import com.nanotasks.Tasks;

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
    public void getContactFromApi(final ContactInfoReq contactInfoReq, final ActionCallbackListener<ActionRespon<FriendVo>> actionCallbackListener) {

        ApiAction.getInstance().getContactInfo(contactInfoReq, new ApiCallbackListener() {
            @Override
            public void onSuccess(final String data) {
                Tasks.executeInBackground(context, new BackgroundWork<ActionRespon<FriendVo>>() {
                    @Override
                    public ActionRespon<FriendVo> doInBackground() throws Exception {
                        ApiRespon<UserRes> result = GsonParser.getInstance().fromJson(data, new TypeToken<ApiRespon<UserRes>>() {
                        }.getType());
                        DBManager.getInstance().saveUser(result.getData());
                        return ActionRespon.getActionRespon(result.getMessage(), result.getRetCode(), DBManager.getInstance().getFriendVo(contactInfoReq.getUserNo()));
                    }
                }, new Completion<ActionRespon<FriendVo>>() {
                    @Override
                    public void onSuccess(Context context, ActionRespon<FriendVo> result) {
                        actionCallbackListener.onSuccess(result);
                    }
                    @Override
                    public void onError(Context context, Exception e) {
                        actionCallbackListener.onSuccess(ActionRespon.<FriendVo>getActionResponError());
                    }
                });

            }

            @Override
            public void onFailure(int errorCode) {
                actionCallbackListener.onFailure(errorCode);
            }
        });
    }

    @Override
    public void getContactFromLocal(final long userNo,  final ActionCallbackListener<ActionRespon<FriendVo>> actionCallbackListener) {
        FriendVo friendVo = ContactTemper.getFriendVo(userNo);
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
                    actionCallbackListener.onSuccess(result);
                }

                @Override
                public void onError(Context context, Exception e) {
                    actionCallbackListener.onSuccess(ActionRespon.<FriendVo>getActionResponError());
                }
            });
        }else{
            actionCallbackListener.onSuccess(ActionRespon.getActionRespon(friendVo));
        }
    }

    @Override
    public List<PartyVo> getPartyVoList(){
        List<PartyVo> partyVoList = DBManager.getInstance().getPartyVoList();
        Collections.sort(partyVoList, new PinyinComparator());
        if(partyVoList.size() == 1){
            char capital = PinyinUtil.getHeadCapitalByChar(partyVoList.get(0).getDisplayName().charAt(0));
            partyVoList.get(0).setSortLetter((capital >= 'A' && capital <= 'Z') ? (capital + "") : "#");
        }
        return partyVoList;
    }

    @Override
    public void getContactList(final ActionCallbackListener<ActionRespon<List<FriendVo>>> actionCallbackListener){
        //先从本地加载并显示
        Tasks.executeInBackground(context, new BackgroundWork<ActionRespon<List<FriendVo>>>() {
            @Override
            public ActionRespon<List<FriendVo>> doInBackground() throws Exception {
                return ActionRespon.getActionRespon(getFriendVoListFromDB());
            }
        }, new Completion<ActionRespon<List<FriendVo>>>() {
            @Override
            public void onSuccess(Context context, ActionRespon<List<FriendVo>> result) {
                actionCallbackListener.onSuccess(result);
                String updateTime = ACache.getInstance().getAsString(Constant.ACACHE_LAST_TIME_CONTACT);
                //TODO 完善后，更改全量获取联系人的时间间隔
                if(Common.isEmpty(updateTime) || Common.getHourDifferenceBetweenTime(updateTime) > 1){
                    getContactListFromApi(actionCallbackListener);
                }
            }

            @Override
            public void onError(Context context, Exception e) {
                actionCallbackListener.onSuccess(ActionRespon.<List<FriendVo>>getActionResponError());
            }
        });
    }

    private void getContactListFromApi(final ActionCallbackListener<ActionRespon<List<FriendVo>>> actionCallbackListener){
        ApiAction.getInstance().getContactList(new ApiCallbackListener() {
            @Override
            public void onSuccess(final String data) {
                Tasks.executeInBackground(context, new BackgroundWork<ActionRespon<List<FriendVo>>>() {
                    @Override
                    public ActionRespon<List<FriendVo>> doInBackground() throws Exception {
                        ApiRespon<List<ContactDto>> result = GsonParser.getInstance().fromJson(data, new TypeToken<ApiRespon<List<ContactDto>>>() {
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
                        ACache.getInstance().put(Constant.ACACHE_LAST_TIME_CONTACT, Common.getCurrentTimeYYMMDD_HHMMSS());
                        actionCallbackListener.onSuccess(result);
                    }

                    @Override
                    public void onError(Context context, Exception e) {
                        actionCallbackListener.onSuccess(ActionRespon.<List<FriendVo>>getActionResponError());
                    }
                });
            }

            @Override
            public void onFailure(int errorCode) {
                actionCallbackListener.onFailure(errorCode);
            }
        });
    }

    private List<FriendVo> getFriendVoListFromDB(){
        List<FriendVo> friendVoList = DBManager.getInstance().getFriendVoList();
        Collections.sort(friendVoList, new PinyinComparator());
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
            if(contactDto.getType() == Constant.CONTACT_FRIEND){
                friendList.add(getFriendFromContactDto(contactDto));
                userList.add(getUserFromContactDto(contactDto));
            }else if(contactDto.getType() == Constant.CONTACT_PARTY){
                partyList.add(getPartyFromContactDto(contactDto));
                memberList.addAll(getMemberList(contactDto));
            }
        }
        DBManager.getInstance().saveUserList(userList);
        DBManager.getInstance().saveFriendList(friendList);
        DBManager.getInstance().savePartyList(partyList);
        DBManager.getInstance().saveMemberList(memberList);
    }

    private List<Member> getMemberList(ContactDto contactDto){
        List<Member> memberList = new ArrayList<>();
        List<MemberDto> memberDtoList = new ArrayList<>();

        memberDtoList.addAll(contactDto.getMemberList());
        Member member;
        for(MemberDto memberDto : memberDtoList){
            member = new Member();
            member.setPartyNo(memberDto.getPartyNo());
            member.setUserNo(memberDto.getUserNo());
            member.setAddTime(memberDto.getAddTime());
            member.setMemberName(memberDto.getMemberName());
            member.setType(memberDto.getType());

            memberList.add(member);
        }

        return memberList;
    }

    private User getUserFromContactDto(ContactDto contactDto){
        User user = new User();
        user.setGender(contactDto.getGender());
        user.setPicPath(contactDto.getPicPath());
        user.setUserNo(contactDto.getContNo());
        user.setSmallNick(contactDto.getSmallNick());
        user.setAutograph(contactDto.getAutograph());
        user.setNickName(contactDto.getNickName());
        user.setHomeTown(contactDto.getHomeTown());

        return user;
    }

    private Party getPartyFromContactDto(ContactDto contactDto){
        Party party = new Party();
        party.setInformation(contactDto.getInformation());
        party.setOwnerNo(contactDto.getOwnerNo());
        party.setPartyName(contactDto.getDisplayName());
        party.setPartyNo(contactDto.getContNo());
        party.setPicPath(contactDto.getPicPath());
        party.setRegisterTime(contactDto.getStartTime());

        return party;
    }

    private Friend getFriendFromContactDto(ContactDto contactDto){
        Friend friend = new Friend();
        friend.setFriendNo(contactDto.getContNo());
        friend.setOwnerNo(CustomSessionPreference.getInstance().getCustomSession().getUserNo());
        friend.setRemark(contactDto.getDisplayName());

        return friend;
    }

}
