package com.bili.diushoujuaner.model.tempHelper;

import com.bili.diushoujuaner.utils.entity.vo.FriendVo;
import com.bili.diushoujuaner.utils.entity.vo.MemberVo;
import com.bili.diushoujuaner.utils.entity.vo.PartyVo;

import java.util.HashMap;
import java.util.Hashtable;

/**
 * Created by BiLi on 2016/3/23.
 * 一级缓存
 * 存放联系人的信息
 */
public class ContactTemper {

    //<FriendNo, FriendVo>
    private Hashtable<Long, FriendVo> friendVoHashtable;
    //<PartyNo, PartyVo>
    private Hashtable<Long, PartyVo> partyVoHashtable;
    //<PartyNo, HashMap<MemberNo, MemberVo>>
    private Hashtable<Long, HashMap<Long, MemberVo>> memberVoHashTable;

    private static ContactTemper contactTemper;

    public ContactTemper(){
        friendVoHashtable = new Hashtable<>();
        partyVoHashtable = new Hashtable<>();
        memberVoHashTable = new Hashtable<>();
    }

    public static synchronized ContactTemper getInstance(){
        if(contactTemper == null){
            contactTemper = new ContactTemper();
        }
        return contactTemper;
    }

    public void addFriendVo(FriendVo friendVo){
        friendVoHashtable.put(friendVo.getFriendNo(), friendVo);
    }

    public void addPartyVo(PartyVo partyVo){
        partyVoHashtable.put(partyVo.getPartyNo(), partyVo);
    }

    public void addMemberVo(MemberVo memberVo){
        if(memberVoHashTable.get(memberVo.getPartyNo()) == null){
            HashMap<Long, MemberVo> memberVoHashMap = new HashMap<>();
            memberVoHashMap.put(memberVo.getUserNo(), memberVo);

            memberVoHashTable.put(memberVo.getPartyNo(), memberVoHashMap);
        }else{
            memberVoHashTable.get(memberVo.getPartyNo()).put(memberVo.getUserNo(), memberVo);
        }
    }

    public MemberVo getMemberVo(long partyNo, long memberNo){
        if(memberVoHashTable.get(partyNo) == null){
            return null;
        }else{
            return memberVoHashTable.get(partyNo).get(memberNo);
        }
    }

    public FriendVo getFriendVo(Long friendNo){
        return friendVoHashtable.get(friendNo);
    }

    public PartyVo getPartyVo(Long partyNo){
        return partyVoHashtable.get(partyNo);
    }

    public String getFriendRemark(Long friendNo){
        FriendVo friendVo = getFriendVo(friendNo);
        return friendVo != null ? friendVo.getDisplayName() : null;
    }

    public String getPartyName(Long partyNo){
        PartyVo partyVo = getPartyVo(partyNo);
        return partyVo != null ? partyVo.getDisplayName() : null;
    }

    public void clearFriend(){
        friendVoHashtable.clear();
    }

    public void clear(){
        friendVoHashtable.clear();
        partyVoHashtable.clear();
        memberVoHashTable.clear();
    }

}
