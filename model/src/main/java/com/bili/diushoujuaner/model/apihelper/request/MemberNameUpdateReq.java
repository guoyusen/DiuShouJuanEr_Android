package com.bili.diushoujuaner.model.apihelper.request;

/**
 * Created by BiLi on 2016/4/22.
 */
public class MemberNameUpdateReq {

    private long partyNo;
    private String memberName;

    public MemberNameUpdateReq(String memberName, long partyNo) {
        this.memberName = memberName;
        this.partyNo = partyNo;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public long getPartyNo() {
        return partyNo;
    }

    public void setPartyNo(long partyNo) {
        this.partyNo = partyNo;
    }
}
