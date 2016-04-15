package com.bili.diushoujuaner.model.actionhelper.action;

import android.content.Context;

import com.bili.diushoujuaner.model.actionhelper.IContactAction;
import com.bili.diushoujuaner.model.actionhelper.respon.ActionRespon;
import com.bili.diushoujuaner.model.apihelper.ApiRespon;
import com.bili.diushoujuaner.model.apihelper.api.ApiAction;
import com.bili.diushoujuaner.model.apihelper.callback.ApiStringCallbackListener;
import com.bili.diushoujuaner.model.apihelper.request.ContactInfoReq;
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
import com.bili.diushoujuaner.utils.Common;
import com.bili.diushoujuaner.utils.Constant;
import com.bili.diushoujuaner.utils.GsonParser;
import com.bili.diushoujuaner.utils.entity.vo.FriendVo;
import com.bili.diushoujuaner.utils.entity.vo.PartyVo;
import com.bili.diushoujuaner.utils.pinyin.PinyinComparator;
import com.bili.diushoujuaner.utils.pinyin.PinyinUtil;
import com.bili.diushoujuaner.utils.entity.dto.ContactDto;
import com.bili.diushoujuaner.utils.entity.dto.MemberDto;
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
    public void getContactFromApi(final ContactInfoReq contactInfoReq, final ActionStringCallbackListener<ActionRespon<FriendVo>> actionStringCallbackListener) {

        ApiAction.getInstance().getContactInfo(contactInfoReq, new ApiStringCallbackListener() {
            @Override
            public void onSuccess(final String data) {
                Tasks.executeInBackground(context, new BackgroundWork<ActionRespon<FriendVo>>() {
                    @Override
                    public ActionRespon<FriendVo> doInBackground() throws Exception {
                        ApiRespon<UserDto> result = GsonParser.getInstance().fromJson(data, new TypeToken<ApiRespon<UserDto>>() {
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
        Collections.sort(partyVoList, new PinyinComparator());
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
                String updateTime = ACache.getInstance().getAsString(Constant.ACACHE_LAST_TIME_CONTACT);
                //TODO 完善后，更改全量获取联系人的时间间隔
                if(Common.isEmpty(updateTime) || Common.getHourDifferenceBetweenTime(updateTime) > 1){
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
        List<User> userList = new ArrayList<>();

        memberDtoList.addAll(contactDto.getMemberList());
        for(MemberDto memberDto : memberDtoList){
            Member member = new Member();
            member.setPartyNo(memberDto.getPartyNo());
            member.setUserNo(memberDto.getUserNo());
            member.setAddTime(memberDto.getAddTime());
            member.setMemberName(memberDto.getMemberName());
            member.setType(memberDto.getType());

            User user = new User();
            user.setUserNo(memberDto.getUserNo());
            user.setPicPath(memberDto.getPicPath());
            userList.add(user);

            memberList.add(member);
        }
        DBManager.getInstance().saveUserListSelective(userList);

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
        user.setWallPaper(contactDto.getWallPaper());

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
