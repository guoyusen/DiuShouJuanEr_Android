package com.bili.diushoujuaner.model.preferhelper;

import android.content.Context;
import android.content.SharedPreferences;

import com.bili.diushoujuaner.utils.response.CustomSession;

/**
 * Created by BiLi on 2016/3/10.
 */
public class CustomSessionPreference {

    private static String FILENAME = "ACCESS_TOKEN";
    private static Context mContext;
    private static CustomSessionPreference customSessionPreference;

    public static void initialize(Context context){
        mContext = context;
        customSessionPreference = new CustomSessionPreference();
    }

    public static synchronized CustomSessionPreference getInstance(){
        if(customSessionPreference == null){
            throw new NullPointerException("CustomSessionPreference was not initialized!");
        }
        return customSessionPreference;
    }

    public void saveAccessToken(CustomSession customSession){
        SharedPreferences preferences = mContext.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        if (customSession.getAccessToken() != null && !(customSession.getAccessToken() == "")) {
            editor.putString("accessToken", customSession.getAccessToken());
        }
        editor.apply();
    }

    public CustomSession getAccessToken(){
        SharedPreferences preferences = mContext.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
        CustomSession customSession = new CustomSession();

        customSession.setAccessToken(preferences.getString("accessToken", ""));

        return customSession;
    }

    public boolean isLogined(){
        CustomSession customSession = getAccessToken();
        if(customSession == null || customSession.getAccessToken().trim().length() <= 0){
            return false;
        }
        return true;
    }

    public void clear(){
        SharedPreferences preferences = mContext.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }

}
