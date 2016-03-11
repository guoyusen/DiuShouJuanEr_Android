package com.bili.diushoujuaner.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.bili.diushoujuaner.model.entities.UserDto;

/**
 * Created by BiLi on 2016/3/10.
 */
public class UserInfoPreference {

    private static String FILENAME = "USER_INFO";
    private static Context mContext;
    private static UserInfoPreference userInfoPreference;

    public UserInfoPreference(Context context){
        mContext = context;
    }

    public static synchronized UserInfoPreference getInstance(){
        if(userInfoPreference == null){
            userInfoPreference = new UserInfoPreference(mContext);
        }
        return userInfoPreference;
    }

    public void saveUserInfo(UserDto userDto){
        SharedPreferences preferences = mContext.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        if (userDto.getAutograph() != null && !userDto.getAutograph().equals("")) {
            editor.putString("autograph", userDto.getAutograph());
        }
        if (userDto.getBirthday() != null && !userDto.getBirthday().equals("")) {
            editor.putString("birthday", userDto.getBirthday());
        }
        if (userDto.getGender() != null) {
            editor.putString("gender", userDto.getGender().toString());
        }
        if (userDto.getHomeTown() != null && !userDto.getHomeTown().equals("")) {
            editor.putString("homeTown", userDto.getHomeTown());
        }
        if (userDto.getLocation() != null && !userDto.getLocation().equals("")) {
            editor.putString("location", userDto.getLocation());
        }
        if (userDto.getMobile() != null && !userDto.getMobile().equals("")) {
            editor.putString("mobile", userDto.getMobile());
        }
        if (userDto.getNickName() != null && !userDto.getNickName().equals("")) {
            editor.putString("nickName", userDto.getNickName());
        }
        if (userDto.getPicPath() != null && !userDto.getPicPath().equals("")) {
            editor.putString("picPath", userDto.getPicPath());
        }
        if (userDto.getRealName() != null && !userDto.getRealName().equals("")) {
            editor.putString("realName", userDto.getRealName());
        }
        if (userDto.getSmallNick() != null && !userDto.getSmallNick().equals("")) {
            editor.putString("smallNick", userDto.getSmallNick());
        }
        if (userDto.getUserNo() != null && !(userDto.getUserNo().intValue() == 0)) {
            editor.putString("userNo", userDto.getUserNo().toString());
        }
        if (userDto.getAccessToken() != null && !(userDto.getAccessToken() == "")) {
            editor.putString("accessToken", userDto.getAccessToken());
        }
    }

    public UserDto getUserInfo(){
        SharedPreferences preferences = mContext.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
        UserDto userDto = new UserDto();

        userDto.setAutograph(preferences.getString("autograph",""));
        userDto.setBirthday(preferences.getString("birthday", ""));
        userDto.setGender(preferences.getInt("gender", 0));
        userDto.setHomeTown(preferences.getString("homeTown", ""));
        userDto.setLocation(preferences.getString("location", ""));
        userDto.setMobile(preferences.getString("mobile", ""));
        userDto.setNickName(preferences.getString("nickName", ""));
        userDto.setPicPath(preferences.getString("picPath", ""));
        userDto.setRealName(preferences.getString("realName", ""));
        userDto.setSmallNick(preferences.getString("smallNick", ""));
        userDto.setUserNo(preferences.getLong("userNo", 0));
        userDto.setAccessToken(preferences.getString("accessToken",""));

        return userDto;
    }

    public void clear(){
        SharedPreferences preferences = mContext.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }

}
