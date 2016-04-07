package com.bili.diushoujuaner.model.databasehelper;

import com.bili.diushoujuaner.model.databasehelper.dao.Friend;
import com.bili.diushoujuaner.model.databasehelper.dao.Member;
import com.bili.diushoujuaner.model.databasehelper.dao.Party;
import com.bili.diushoujuaner.model.databasehelper.dao.User;
import com.bili.diushoujuaner.utils.Common;
import com.bili.diushoujuaner.model.apihelper.response.UserRes;

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
        user.setSmallNick(userRes.getSmallNick());
        user.setUserNo(userRes.getUserNo());
        user.setRegistTime(userRes.getRegistTime());
        user.setWallPaper(userRes.getWallPaper());
        user.setEmail(userRes.getEmail());

        return user;
    }

    public static User updateUserByUser(User older, User newer){
        older.setMobile(newer.getMobile());
        older.setAutograph(newer.getAutograph());
        older.setBirthday(newer.getBirthday());
        older.setHomeTown(newer.getHomeTown());
        older.setLocation(newer.getLocation());
        older.setNickName(newer.getNickName());
        older.setPicPath(newer.getPicPath());
        older.setSmallNick(newer.getSmallNick());
        older.setRegistTime(newer.getRegistTime());
        older.setGender(newer.getGender());
        older.setUpdateTime(newer.getUpdateTime());
        older.setWallPaper(newer.getWallPaper());
        older.setEmail(newer.getEmail());

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
