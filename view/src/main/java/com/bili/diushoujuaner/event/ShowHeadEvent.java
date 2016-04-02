package com.bili.diushoujuaner.event;

/**
 * Created by BiLi on 2016/4/2.
 */
public class ShowHeadEvent {

    public ShowHeadEvent(String headPicUrl) {
        this.headPicUrl = headPicUrl;
    }

    private String headPicUrl;

    public String getHeadPicUrl() {
        return headPicUrl;
    }

    public void setHeadPicUrl(String headPicUrl) {
        this.headPicUrl = headPicUrl;
    }
}
