package com.bili.diushoujuaner.model.preferhelper;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by BiLi on 2016/3/10.
 */
public class HomeStatePreference {

    private static String FILENAME = "RUNNING";
    private static Context mContext;
    private static HomeStatePreference homeStatePreference;

    public static void initialize(Context context){
        mContext = context;
        homeStatePreference = new HomeStatePreference();
    }

    public static synchronized HomeStatePreference getInstance(){
        if(homeStatePreference == null){
            throw new NullPointerException("SettingPreference was not initialized!");
        }
        return homeStatePreference;
    }

    public void saveState(boolean isBackground){
        SharedPreferences preferences = mContext.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putBoolean("isBackground", isBackground);
        editor.apply();
    }

    public boolean isBackground(){
        SharedPreferences preferences = mContext.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
        return preferences.getBoolean("isBackground", false);
    }

    public void clear(){
        SharedPreferences preferences = mContext.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }

}
