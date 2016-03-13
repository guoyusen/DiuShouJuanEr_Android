package com.bili.diushoujuaner.model.databasehelper;

import com.bili.diushoujuaner.model.databasehelper.dao.User;
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
        older.setMobile(newer.getMobile());
        older.setAutograph(newer.getAutograph());
        older.setBirthday(newer.getBirthday());
        older.setHomeTown(newer.getHomeTown());
        older.setLocation(newer.getLocation());
        older.setNickName(newer.getNickName());
        older.setGender(newer.getGender());
        older.setPicPath(newer.getPicPath());
        older.setRealName(newer.getRealName());
        older.setSmallNick(newer.getSmallNick());
        older.setUserNo(newer.getUserNo());
        older.setRegistTime(newer.getRegistTime());
        return older;
    }

}
