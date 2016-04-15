package com.bili.diushoujuaner.utils.entity.dto;

/**
 * Created by BiLi on 2016/4/12.
 */
public class OffMsgDto {

    private Long fromNo;

    private Long toNo;

    private String content;

    private String time;

    private Boolean isRead;

    private Integer msgType;

    private Integer conType;

    public Long getToNo() {
        return toNo;
    }

    public void setToNo(Long toNo) {
        this.toNo = toNo;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getConType() {
        return conType;
    }

    public void setConType(Integer conType) {
        this.conType = conType;
    }

    public Long getFromNo() {
        return fromNo;
    }

    public void setFromNo(Long fromNo) {
        this.fromNo = fromNo;
    }

    public Boolean getRead() {
        return isRead;
    }

    public void setRead(Boolean read) {
        isRead = read;
    }

    public Integer getMsgType() {
        return msgType;
    }

    public void setMsgType(Integer msgType) {
        this.msgType = msgType;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
