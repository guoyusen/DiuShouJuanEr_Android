package com.bili.diushoujuaner.utils.entity.vo;

/**
 * Created by BiLi on 2016/4/24.
 */
public class ApplyVo {

    private long fromNo;
    private long toNo;
    private String content;
    private String time;
    private Integer type;
    private Boolean read;
    private Boolean accept;
    private String picPath;
    private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public long getFromNo() {
        return fromNo;
    }

    public void setFromNo(long fromNo) {
        this.fromNo = fromNo;
    }

    public long getToNo() {
        return toNo;
    }

    public void setToNo(long toNo) {
        this.toNo = toNo;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Boolean getRead() {
        return read;
    }

    public void setRead(Boolean read) {
        this.read = read;
    }

    public Boolean getAccept() {
        return accept;
    }

    public void setAccept(Boolean accept) {
        this.accept = accept;
    }

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }
}
