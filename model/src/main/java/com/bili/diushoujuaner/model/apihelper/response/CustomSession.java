package com.bili.diushoujuaner.model.apihelper.response;

/**
 * Created by BiLi on 2016/3/11.
 */
public class CustomSession {

    private String accessToken;

    private String lastTime;

    private Long userNo;

    public String getLastTime() {
        return lastTime;
    }

    public void setLastTime(String lastTime) {
        this.lastTime = lastTime;
    }

    public Long getUserNo() {
        return userNo;
    }

    public void setUserNo(Long userNo) {
        this.userNo = userNo;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
