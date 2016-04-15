package com.bili.diushoujuaner.model.apihelper.request;

/**
 * Created by BiLi on 2016/4/12.
 */
public class RecallPublishReq {

    private String content;
    private int piCount;
    private String serial;

    public RecallPublishReq(String content, int piCount, String serial) {
        this.content = content;
        this.piCount = piCount;
        this.serial = serial;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getPiCount() {
        return piCount;
    }

    public void setPiCount(int piCount) {
        this.piCount = piCount;
    }
}
