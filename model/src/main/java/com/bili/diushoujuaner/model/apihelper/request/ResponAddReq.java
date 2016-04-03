package com.bili.diushoujuaner.model.apihelper.request;

/**
 * Created by BiLi on 2016/4/2.
 */
public class ResponAddReq {

    private long commentNo;
    private long toNo;
    private String content;
    private String timeStamp;

    public long getCommentNo() {
        return commentNo;
    }

    public void setCommentNo(long commentNo) {
        this.commentNo = commentNo;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public long getToNo() {
        return toNo;
    }

    public void setToNo(long toNo) {
        this.toNo = toNo;
    }
}
