package com.bili.diushoujuaner.model.apihelper.request;

/**
 * Created by BiLi on 2016/4/24.
 */
public class FriendAgreeReq {

    private long fromNo;
    private long toNo;

    public FriendAgreeReq(long fromNo, long toNo) {
        this.fromNo = fromNo;
        this.toNo = toNo;
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
}
