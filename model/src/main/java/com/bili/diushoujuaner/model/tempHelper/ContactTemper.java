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
    private static Hashtable<Long, FriendVo> friendVoHashtable = new Hashtable<>();
    //<PartyNo, PartyVo>
    private static Hashtable<Long, PartyVo> partyVoHashtable = new Hashtable<>();
    //<PartyNo, HashMap<MemberNo, MemberVo>>
    private static Hashtable<Long, HashMap<Long, MemberVo>> memberVoHashTable = new Hashtable<>();

    public static void addFriendVo(FriendVo friendVo){
        friendVoHashtable.put(friendVo.getFriendNo(), friendVo);
    }

    public static void addPartyVo(PartyVo partyVo){
        partyVoHashtable.put(partyVo.getPartyNo(), partyVo);
    }

    public static void addMemberVo(MemberVo memberVo){
        if(memberVoHashTable.get(memberVo.getPartyNo()) == null){
            HashMap<Long, MemberVo> memberVoHashMap = new HashMap<>();
            memberVoHashMap.put(memberVo.getUserNo(), memberVo);

            memberVoHashTable.put(memberVo.getPartyNo(), memberVoHashMap);
        }else{
            memberVoHashTable.get(memberVo.getPartyNo()).put(memberVo.getUserNo(), memberVo);
        }
    }

    public static MemberVo getMemberVo(long partyNo, long memberNo){
        if(memberVoHashTable.get(partyNo) == null){
            return null;
        }else{
            return memberVoHashTable.get(partyNo).get(memberNo);
        }
    }

    public static FriendVo getFriendVo(Long friendNo){
        return friendVoHashtable.get(friendNo);
    }

    public static PartyVo getPartyVo(Long partyNo){
        return partyVoHashtable.get(partyNo);
    }

    public static String getFriendRemark(Long friendNo){
        FriendVo friendVo = getFriendVo(friendNo);
        return friendVo != null ? friendVo.getDisplayName() : null;
    }

    public static String getPartyName(Long partyNo){
        PartyVo partyVo = getPartyVo(partyNo);
        return partyVo != null ? partyVo.getDisplayName() : null;
    }

    public static void clearFriend(){
        friendVoHashtable.clear();
    }

    public static void clear(){
        friendVoHashtable.clear();
        partyVoHashtable.clear();
        memberVoHashTable.clear();
    }

}
