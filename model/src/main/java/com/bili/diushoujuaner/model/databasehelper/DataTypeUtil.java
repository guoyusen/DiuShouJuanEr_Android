package com.bili.diushoujuaner.model.databasehelper;

import com.bili.diushoujuaner.model.databasehelper.dao.Friend;
import com.bili.diushoujuaner.model.databasehelper.dao.Member;
import com.bili.diushoujuaner.model.databasehelper.dao.Party;
import com.bili.diushoujuaner.model.databasehelper.dao.User;
import com.bili.diushoujuaner.utils.Common;
import com.bili.diushoujuaner.utils.response.UserRes;

/**
 * Created by BiLi on 2016/3/13.
 */
public class DataTypeUtil {

    public static User changeUserResToUser(UserRes userRes){
        User user = new User();
        user.setMobile(userRes.getMobile());
        user.setAutograph(userRes.getAutograph());
        user.setBirthday(userRes.getBirthday());
        user.setHomeTown(userRes.getHomeTown());
        user.setLocation(userRes.getLocation());
        user.setNickName(userRes.getNickName());
        user.setGender(userRes.getGender());
        user.setPicPath(userRes.getPicPath());
        user.setRealName(userRes.getRealName());
        user.setSmallNick(userRes.getSmallNick());
        user.setUserNo(userRes.getUserNo());
        user.setRegistTime(userRes.getRegistTime());

        return user;
    }

    public static User updateUserByUser(User older, User newer){
        older.setMobile(Common.isEmpty(newer.getMobile()) ? older.getMobile() : newer.getMobile());
        older.setAutograph(Common.isEmpty(newer.getAutograph()) ? older.getAutograph() : newer.getAutograph());
        older.setBirthday(Common.isEmpty(newer.getBirthday()) ? older.getBirthday() : newer.getBirthday());
        older.setHomeTown(Common.isEmpty(newer.getHomeTown()) ? older.getHomeTown() : newer.getHomeTown());
        older.setLocation(Common.isEmpty(newer.getLocation()) ? older.getLocation() : newer.getLocation());
        older.setNickName(Common.isEmpty(newer.getNickName()) ? older.getNickName() : newer.getNickName());
        older.setPicPath(Common.isEmpty(newer.getPicPath()) ? older.getPicPath() : newer.getPicPath());
        older.setRealName(Common.isEmpty(newer.getRealName()) ? older.getRealName() : newer.getRealName());
        older.setSmallNick(Common.isEmpty(newer.getSmallNick()) ? older.getSmallNick() : newer.getSmallNick());
        older.setRegistTime(Common.isEmpty(newer.getRegistTime()) ? older.getRegistTime() : newer.getRegistTime());
        older.setGender(newer.getGender());

        return older;
    }

    public static Friend updateFriendByFriend(Friend older, Friend newer){
        older.setRemark(Common.isEmpty(newer.getRemark()) ? older.getRemark() : newer.getRemark());

        return older;
    }

    public static Party updatePartyByParty(Party older, Party newer){
        older.setPicPath(Common.isEmpty(newer.getPicPath()) ? older.getPicPath() : newer.getPicPath());
        older.setPartyName(Common.isEmpty(newer.getPartyName()) ? older.getPartyName() : newer.getPartyName());
        older.setInformation(Common.isEmpty(newer.getInformation()) ? older.getPicPath() : newer.getInformation());

        return older;
    }

    public static Member updateMemberByMember(Member older, Member newer){
        older.setMemberName(Common.isEmpty(newer.getMemberName()) ? older.getMemberName() : newer.getMemberName());
        older.setType(newer.getType());

        return older;
    }

}
