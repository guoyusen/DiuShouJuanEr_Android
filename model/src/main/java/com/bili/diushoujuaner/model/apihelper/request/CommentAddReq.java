package com.bili.diushoujuaner.model.apihelper.request;

/**
 * Created by BiLi on 2016/4/2.
 */
public class CommentAddReq {

    private String timeStamp;
    private long recallNo;
    private String content;

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getRecallNo() {
        return recallNo;
    }

    public void setRecallNo(long recallNo) {
        this.recallNo = recallNo;
    }
}
