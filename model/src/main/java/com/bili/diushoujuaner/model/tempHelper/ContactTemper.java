package com.bili.diushoujuaner.model.tempHelper;

import com.bili.diushoujuaner.utils.entity.FriendVo;
import com.bili.diushoujuaner.utils.entity.PartyVo;

import java.util.Hashtable;

/**
 * Created by BiLi on 2016/3/23.
 */
public class ContactTemper {

    private static Hashtable<Long,FriendVo> friendVoHashtable = new Hashtable<>();
    private static Hashtable<Long,PartyVo> partyVoHashtable = new Hashtable<>();

    public static void addFriendVo(FriendVo friendVo){
        friendVoHashtable.put(friendVo.getFriendNo(), friendVo);
    }

    public static FriendVo getFriendVo(Long friendNo){
        return friendVoHashtable.get(friendNo);
    }

    public static String getFriendRemark(Long friendNo){
        FriendVo friendVo = getFriendVo(friendNo);
        if(friendVo != null){
            return friendVo.getDisplayName();
        }
        return null;
    }

    public static void addPartyVo(PartyVo partyVo){
        partyVoHashtable.put(partyVo.getPartyNo(), partyVo);
    }

    public static PartyVo getPartyNo(Long partyNo){
        return partyVoHashtable.get(partyNo);
    }

    public static String getPartyName(Long partyNo){
        PartyVo partyVo = getPartyNo(partyNo);
        if(partyVo != null){
            return partyVo.getDisplayName();
        }
        return null;
    }

    public static void clearFriend(){
        friendVoHashtable.clear();
    }

}
