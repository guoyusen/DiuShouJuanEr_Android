package com.bili.diushoujuaner.model.preferhelper;

import android.content.Context;
import android.content.SharedPreferences;

import com.bili.diushoujuaner.utils.entity.vo.SettingVo;

/**
 * Created by BiLi on 2016/3/10.
 */
public class SettingPreference {

    private static String FILENAME = "SETTINGS_";
    private static Context mContext;
    private static SettingPreference settingPreference;

    public static void initialize(Context context){
        mContext = context;
        settingPreference = new SettingPreference();
    }

    public static synchronized SettingPreference getInstance(){
        if(settingPreference == null){
            throw new NullPointerException("SettingPreference was not initialized!");
        }
        return settingPreference;
    }

    public void saveSettings(SettingVo settingVo){
        SharedPreferences preferences = mContext.getSharedPreferences(FILENAME + CustomSessionPreference.getInstance().getCustomSession().getUserNo(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putBoolean("messageVoice", settingVo.isMessageVoice());
        editor.putBoolean("messageVibrate", settingVo.isMessageVibrate());
        editor.putBoolean("homeFriend", settingVo.isHomeFriend());
        editor.putBoolean("imageGprs", settingVo.isImageGprs());

        editor.apply();
    }

    public SettingVo getSetting(){
        SharedPreferences preferences = mContext.getSharedPreferences(FILENAME + CustomSessionPreference.getInstance().getCustomSession().getUserNo(), Context.MODE_PRIVATE);
        SettingVo settingVo = new SettingVo();
        settingVo.setIsHomeFriend(preferences.getBoolean("homeFriend", false));
        settingVo.setIsImageGprs(preferences.getBoolean("imageGprs", false));
        settingVo.setIsMessageVibrate(preferences.getBoolean("messageVibrate", false));
        settingVo.setIsMessageVoice(preferences.getBoolean("messageVoice", false));

        return settingVo;
    }

    public void clear(){
        SharedPreferences preferences = mContext.getSharedPreferences(FILENAME + CustomSessionPreference.getInstance().getCustomSession().getUserNo(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }

}
