package com.bili.diushoujuaner.utils.entity.vo;

/**
 * Created by BiLi on 2016/4/13.
 */
public class MessageVo {

    private String serialNo;
    private long id;//分页获取信息的标志
    private long fromNo;//消息发送者的账号
    private long toNo;//接收者的账号，单聊时为好友账号，群聊时为群号
    private String time;//消息的时间
    private String content;//消息的内容
    private int status;//消息的状态 正在发送，发送失败，发送成功
    private int msgType;//消息的类型 好友消息  群消息
    private int conType;//消息的文本类型  语音  图片  文字
    private boolean isTimeShow;//时间是否显示
    private boolean isRead;//是否已读

    public MessageVo(){
        this.id = -1;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
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

    public boolean isTimeShow() {
        return isTimeShow;
    }

    public void setTimeShow(boolean timeShow) {
        isTimeShow = timeShow;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getConType() {
        return conType;
    }

    public void setConType(int conType) {
        this.conType = conType;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
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

}
