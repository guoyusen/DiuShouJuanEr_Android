package com.bili.diushoujuaner.model.preferhelper;

import android.content.Context;
import android.content.SharedPreferences;

import com.bili.diushoujuaner.utils.TimeUtil;

/**
 * Created by BiLi on 2016/3/10.
 */
public class ConnectionPreference {

    private static String FILENAME = "CONNECTION";
    private static Context mContext;
    private static ConnectionPreference connectionPreference;

    public static void initialize(Context context){
        mContext = context;
        connectionPreference = new ConnectionPreference();
    }

    public static synchronized ConnectionPreference getInstance(){
        if(connectionPreference == null){
            throw new NullPointerException("SettingPreference was not initialized!");
        }
        return connectionPreference;
    }

    public void saveState(boolean connected){
        SharedPreferences preferences = mContext.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putBoolean("connection", connected);
        editor.putString("lastTime", TimeUtil.getCurrentTimeFull());
        editor.apply();
    }

    public boolean isConnected(){
        SharedPreferences preferences = mContext.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
        return preferences.getBoolean("connection", false);
    }

    public boolean isOverTime(){
        SharedPreferences preferences = mContext.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
        //超过15秒,认为离线
        return TimeUtil.getMilliDifferenceBetweenTime(preferences.getString("lastTime","1970-01-01 08:00:00:000")) > 15000;
    }

    public void clear(){
        SharedPreferences preferences = mContext.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }

}
