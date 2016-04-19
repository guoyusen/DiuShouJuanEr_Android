package com.bili.diushoujuaner.utils.entity.dto;

import java.io.Serializable;

/**
 * Created by BiLi on 2016/4/15.
 */
public class MessageDto implements Serializable {

    private String serialNo;//只是用来更新本地的消息发送状态
    private long senderNo;//发送者的账号
    private long receiverNo;//接收者的账号
    private int msgType;//通信类型 群 Or 单
    private int conType;//文本类型 文本 Or 语音 Or 图片 Or 文件
    private String msgContent = "";//消息内容
    private String msgTime = "";//消息时间

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

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
