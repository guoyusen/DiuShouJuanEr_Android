package com.bili.diushoujuaner.model.apihelper.request;

/**
 * Created by BiLi on 2016/4/3.
 */
public class ContactAddInfoReq {

    private long userNo;

    public ContactAddInfoReq() {
    }

    public ContactAddInfoReq(long userNo) {
        this.userNo = userNo;
    }

    public long getUserNo() {
        return userNo;
    }

    public void setUserNo(long userNo) {
        this.userNo = userNo;
    }
}
