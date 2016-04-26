package com.bili.diushoujuaner.model.apihelper.request;

/**
 * Created by BiLi on 2016/4/26.
 */
public class MemberExitReq {

    private long partyNo;

    public MemberExitReq(long partyNo) {
        this.partyNo = partyNo;
    }

    public long getPartyNo() {
        return partyNo;
    }

    public void setPartyNo(long partyNo) {
        this.partyNo = partyNo;
    }
}
