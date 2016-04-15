package com.bili.diushoujuaner.utils.entity.dto;

import java.io.Serializable;

/**
 * Created by BiLi on 2016/4/15.
 */
public class MessageDto implements Serializable {

    private long senderNo;
    private long receiverNo;
    private int msgType;
    private int conType;
    private String msgContent = "";
    private String msgTime = "";

    public int getConType() {
        return conType;
    }

    public void setConType(int conType) {
        this.conType = conType;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    public String getMsgTime() {
        return msgTime;
    }

    public void setMsgTime(String msgTime) {
        this.msgTime = msgTime;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public long getReceiverNo() {
        return receiverNo;
    }

    public void setReceiverNo(long receiverNo) {
        this.receiverNo = receiverNo;
    }

    public long getSenderNo() {
        return senderNo;
    }

    public void setSenderNo(long senderNo) {
        this.senderNo = senderNo;
    }

    @Override
    public String toString() {
        return "MessageDto{" +
                "conType=" + conType +
                ", senderNo=" + senderNo +
                ", receiverNo=" + receiverNo +
                ", msgType=" + msgType +
                ", msgContent='" + msgContent + '\'' +
                ", msgTime='" + msgTime + '\'' +
                '}';
    }
}
