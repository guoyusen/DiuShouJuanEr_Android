package com.bili.diushoujuaner.model.apihelper.request;

/**
 * Created by BiLi on 2016/4/12.
 */
public class RecallPublishReq {

    private String content;
    private int piCount;

    public RecallPublishReq(String content, int piCount) {
        this.content = content;
        this.piCount = piCount;
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
