package com.bili.diushoujuaner.model.action.impl;

import com.bili.diushoujuaner.model.action.IContactAction;
import com.bili.diushoujuaner.model.action.respon.ActionRespon;
import com.bili.diushoujuaner.model.apihelper.ApiRespon;
import com.bili.diushoujuaner.model.apihelper.api.ApiAction;
import com.bili.diushoujuaner.model.apihelper.callback.ApiCallbackListener;
import com.bili.diushoujuaner.model.callback.ActionCallbackListener;
import com.bili.diushoujuaner.model.databasehelper.DBManager;
import com.bili.diushoujuaner.model.databasehelper.dao.Friend;
import com.bili.diushoujuaner.model.databasehelper.dao.Member;
import com.bili.diushoujuaner.model.databasehelper.dao.Party;
import com.bili.diushoujuaner.model.databasehelper.dao.User;
import com.bili.diushoujuaner.model.preferhelper.CustomSessionPreference;
import com.bili.diushoujuaner.utils.Constant;
import com.bili.diushoujuaner.utils.GsonParser;
import com.bili.diushoujuaner.utils.entity.FriendVo;
import com.bili.diushoujuaner.utils.entity.PartyVo;
import com.bili.diushoujuaner.utils.pinyin.PinyinComparator;
import com.bili.diushoujuaner.utils.pinyin.PinyinUtil;
import com.bili.diushoujuaner.utils.response.ContactDto;
import com.bili.diushoujuaner.utils.response.MemberDto;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by BiLi on 2016/3/15.
 */
public class ContactAction implements IContactAction{

    private static ContactAction contactAction;

    public static synchronized ContactAction getInstance(){
        if(contactAction == null){
            contactAction = new ContactAction();
        }
        return contactAction;
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
        actionCallbackListener.onSuccess(ActionRespon.getActionRespon(Constant.ACTION_LOAD_LOCAL_SUCCESS,Constant.RETCODE_SUCCESS,getFriendVoFrimDB()));
        //TODO 判断是否需要进行全量加载
        ApiAction.getInstance().getContactList(new ApiCallbackListener() {
            @Override
            public void onSuccess(final String data) {
                ApiRespon<List<ContactDto>> result = GsonParser.getInstance().fromJson(data, new TypeToken<ApiRespon<List<ContactDto>>>() {
                }.getType());
                if(result.getIsLegal()){
                    saveContactsFromList(result.getData());
                    actionCallbackListener.onSuccess(ActionRespon.getActionRespon(result.getMessage(), result.getRetCode(), getFriendVoFrimDB()));
                }else{
                    actionCallbackListener.onSuccess(ActionRespon.getActionRespon(result.getMessage(), result.getRetCode(), (List<FriendVo>)null));
                }
            }

            @Override
            public void onFailure(int errorCode) {
                actionCallbackListener.onFailure(errorCode);
            }
        });
    }

    private List<FriendVo> getFriendVoFrimDB(){
        List<FriendVo> friendVoList = DBManager.getInstance().getFriendVo();
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
