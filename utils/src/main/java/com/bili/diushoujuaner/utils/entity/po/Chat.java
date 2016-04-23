package com.bili.diushoujuaner.utils.entity.po;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table "CHAT".
 */
public class Chat {

    private Long id;
    private String serialNo;
    private long ownerNo;
    private long fromNo;
    private long toNo;
    private String content;
    /** Not-null value. */
    private String time;
    private int msgType;
    private int conType;
    private int status;
    private boolean showTime;
    private boolean read;

    public Chat() {
    }

    public Chat(Long id) {
        this.id = id;
    }

    public Chat(Long id, String serialNo, long ownerNo, long fromNo, long toNo, String content, String time, int msgType, int conType, int status, boolean showTime, boolean read) {
        this.id = id;
        this.serialNo = serialNo;
        this.ownerNo = ownerNo;
        this.fromNo = fromNo;
        this.toNo = toNo;
        this.content = content;
        this.time = time;
        this.msgType = msgType;
        this.conType = conType;
        this.status = status;
        this.showTime = showTime;
        this.read = read;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public long getOwnerNo() {
        return ownerNo;
    }

    public void setOwnerNo(long ownerNo) {
        this.ownerNo = ownerNo;
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

    /** Not-null value. */
    public String getTime() {
        return time;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setTime(String time) {
        this.time = time;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public int getConType() {
        return conType;
    }

    public void setConType(int conType) {
        this.conType = conType;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean getShowTime() {
        return showTime;
    }

    public void setShowTime(boolean showTime) {
        this.showTime = showTime;
    }

    public boolean getRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

}