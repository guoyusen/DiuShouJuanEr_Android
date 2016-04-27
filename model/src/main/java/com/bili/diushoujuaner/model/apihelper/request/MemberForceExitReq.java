package com.bili.diushoujuaner.model.apihelper.request;

/**
 * Created by BiLi on 2016/4/27.
 */
public class MemberForceExitReq {

    private long partyNo;
    private long memberNo;

    public MemberForceExitReq(long partyNo, long memberNo) {
        this.partyNo = partyNo;
        this.memberNo = memberNo;
    }

    public long getPartyNo() {
        return partyNo;
    }

    public void setPartyNo(long partyNo) {
        this.partyNo = partyNo;
    }

    public long getMemberNo() {
        return memberNo;
    }

    public void setMemberNo(long memberNo) {
        this.memberNo = memberNo;
    }
}
