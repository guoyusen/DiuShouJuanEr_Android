package com.bili.diushoujuaner.model.preference;

import android.content.Context;
import android.content.SharedPreferences;

import com.bili.diushoujuaner.utils.response.AccessTokenDto;

/**
 * Created by BiLi on 2016/3/10.
 */
public class AccessTokenPreference {

    private static String FILENAME = "ACCESS_TOKEN";
    private static Context mContext;
    private static AccessTokenPreference accessTokenPreference;

    public static void initialize(Context context){
        mContext = context;
        accessTokenPreference = new AccessTokenPreference();
    }

    public static synchronized AccessTokenPreference getInstance(){
        if(accessTokenPreference == null){
            throw new NullPointerException("AccessTokenPreference was not initialized!");
        }
        return accessTokenPreference;
    }

    public void saveAccessToken(AccessTokenDto accessTokenDto){
        SharedPreferences preferences = mContext.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        if (accessTokenDto.getAccessToken() != null && !(accessTokenDto.getAccessToken() == "")) {
            editor.putString("accessToken", accessTokenDto.getAccessToken());
        }
        editor.apply();
    }

    public AccessTokenDto getAccessToken(){
        SharedPreferences preferences = mContext.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
        AccessTokenDto accessTokenDto = new AccessTokenDto();

        accessTokenDto.setAccessToken(preferences.getString("accessToken",""));

        return accessTokenDto;
    }

    public void clear(){
        SharedPreferences preferences = mContext.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }

}
