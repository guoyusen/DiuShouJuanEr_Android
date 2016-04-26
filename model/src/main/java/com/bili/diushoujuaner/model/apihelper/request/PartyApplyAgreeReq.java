package com.bili.diushoujuaner.model.apihelper.request;

/**
 * Created by BiLi on 2016/4/25.
 */
public class PartyApplyAgreeReq {

    private long partyNo;
    private long memberNo;

    public PartyApplyAgreeReq(long partyNo, long memberNo) {
        this.partyNo = partyNo;
        this.memberNo = memberNo;
    }

    public long getMemberNo() {
        return memberNo;
    }

    public void setMemberNo(long memberNo) {
        this.memberNo = memberNo;
    }

    public long getPartyNo() {
        return partyNo;
    }

    public void setPartyNo(long partyNo) {
        this.partyNo = partyNo;
    }

}
