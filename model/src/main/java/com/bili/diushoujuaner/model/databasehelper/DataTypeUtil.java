package com.bili.diushoujuaner.model.databasehelper;

import com.bili.diushoujuaner.utils.entity.po.Friend;
import com.bili.diushoujuaner.utils.entity.po.Member;
import com.bili.diushoujuaner.utils.entity.po.Party;
import com.bili.diushoujuaner.utils.entity.po.User;
import com.bili.diushoujuaner.utils.Common;
import com.bili.diushoujuaner.utils.entity.dto.UserDto;

/**
 * Created by BiLi on 2016/3/13.
 */
public class DataTypeUtil {

    public static User changeUserDtoToUser(UserDto userDto){
        User user = new User();
        user.setMobile(userDto.getMobile());
        user.setAutograph(userDto.getAutograph());
        user.setBirthday(userDto.getBirthday());
        user.setHomeTown(userDto.getHomeTown());
        user.setLocation(userDto.getLocation());
        user.setNickName(userDto.getNickName());
        user.setGender(userDto.getGender());
        user.setPicPath(userDto.getPicPath());
        user.setSmallNick(userDto.getSmallNick());
        user.setUserNo(userDto.getUserNo());
        user.setRegistTime(userDto.getRegistTime());
        user.setWallPaper(userDto.getWallPaper());
        user.setEmail(userDto.getEmail());

        return user;
    }

//    public static User updateUserByNewerSelective(User older, User newer){
//
//    }

    public static User updateUserBySelective(User older, User newer){
        older.setMobile(Common.isEmpty(newer.getMobile()) ? older.getMobile() : newer.getMobile());
        older.setAutograph(Common.isEmpty(newer.getAutograph()) ? older.getAutograph() : newer.getAutograph());
        older.setBirthday(Common.isEmpty(newer.getBirthday()) ? older.getBirthday() : newer.getBirthday());
        older.setHomeTown(Common.isEmpty(newer.getHomeTown()) ? older.getHomeTown() : newer.getHomeTown());
        older.setLocation(Common.isEmpty(newer.getLocation()) ? older.getLocation() : newer.getLocation());
        older.setNickName(Common.isEmpty(newer.getNickName()) ? older.getNickName() : newer.getNickName());
        older.setPicPath(Common.isEmpty(newer.getPicPath()) ? older.getPicPath() : newer.getPicPath());
        older.setSmallNick(Common.isEmpty(newer.getSmallNick()) ? older.getSmallNick() : newer.getSmallNick());
        older.setRegistTime(Common.isEmpty(newer.getRegistTime()) ? older.getRegistTime() : newer.getRegistTime());
        older.setGender(newer.getGender() == null ? older.getGender() : newer.getGender());
        older.setUpdateTime(Common.isEmpty(newer.getUpdateTime()) ? older.getUpdateTime() : newer.getUpdateTime());
        older.setWallPaper(Common.isEmpty(newer.getWallPaper()) ? older.getWallPaper() : newer.getWallPaper());
        older.setEmail(Common.isEmpty(newer.getEmail()) ? older.getEmail() : newer.getEmail());
        older.setUserNo(newer.getUserNo() == null ? older.getUserNo() : older.getUserNo());

        return older;
    }

    public static Friend updateFriendByNewer(Friend older, Friend newer){
        older.setRemark(Common.isEmpty(newer.getRemark()) ? older.getRemark() : newer.getRemark());
        return older;
    }

    public static Member updateMemberByNewer(Member older, Member newer){
        older.setMemberName(Common.isEmpty(newer.getMemberName()) ? older.getMemberName() : newer.getMemberName());
        older.setType(newer.getType());

        return older;
    }

}
