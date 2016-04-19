package com.bili.diushoujuaner.utils.entity.vo;

/**
 * Created by BiLi on 2016/4/13.
 */
public class ChattingVo {

    private String serialNo;//MessageVo对应于数据库中的id
    private long userNo;//对方的账号 1.好友账号 2.群号
    private long memberNo;//群聊时使用
    private int msgType;//消息的类型 1.好友消息 2.群消息
    private String content;//消息内容
    private String time;//消息时间
    private int status;//消息状态 1.正在发送 2.发送失败 3.发送成功
    private int unReadCount;//未读数据
    private int conType;//消息内容类型
    private String lastShowTime;//上次显示的时间

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getLastShowTime() {
        return lastShowTime;
    }

    public void setLastShowTime(String lastShowTime) {
        this.lastShowTime = lastShowTime;
    }

    public long getMemberNo() {
        return memberNo;
    }

    public void setMemberNo(long memberNo) {
        this.memberNo = memberNo;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getUnReadCount() {
        return unReadCount;
    }

    public void setUnReadCount(int unReadCount) {
        this.unReadCount = unReadCount;
    }

    public long getUserNo() {
        return userNo;
    }

    public void setUserNo(long userNo) {
        this.userNo = userNo;
    }
}
