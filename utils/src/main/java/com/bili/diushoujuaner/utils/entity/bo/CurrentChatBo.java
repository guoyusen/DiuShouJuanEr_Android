package com.bili.diushoujuaner.utils.entity.bo;

/**
 * Created by BiLi on 2016/4/13.
 */
public class CurrentChatBo {

    private long toNo;
    private int msgType;
    private boolean isChatting;

    public boolean isChatting() {
        return isChatting;
    }

    public void setChatting(boolean chatting) {
        isChatting = chatting;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public long getToNo() {
        return toNo;
    }

    public void setToNo(long toNo) {
        this.toNo = toNo;
    }
}
