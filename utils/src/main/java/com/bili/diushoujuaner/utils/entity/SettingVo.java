package com.bili.diushoujuaner.utils.entity;

/**
 * Created by BiLi on 2016/4/7.
 */
public class SettingVo {

    private boolean isMessageVoice;
    private boolean isMessageVibrate;
    private boolean isHomeFriend;
    private boolean isImageGprs;

    public boolean isHomeFriend() {
        return isHomeFriend;
    }

    public void setIsHomeFriend(boolean isHomeFriend) {
        this.isHomeFriend = isHomeFriend;
    }

    public boolean isImageGprs() {
        return isImageGprs;
    }

    public void setIsImageGprs(boolean isImageGprs) {
        this.isImageGprs = isImageGprs;
    }

    public boolean isMessageVibrate() {
        return isMessageVibrate;
    }

    public void setIsMessageVibrate(boolean isMessageVibrate) {
        this.isMessageVibrate = isMessageVibrate;
    }

    public boolean isMessageVoice() {
        return isMessageVoice;
    }

    public void setIsMessageVoice(boolean isMessageVoice) {
        this.isMessageVoice = isMessageVoice;
    }
}
