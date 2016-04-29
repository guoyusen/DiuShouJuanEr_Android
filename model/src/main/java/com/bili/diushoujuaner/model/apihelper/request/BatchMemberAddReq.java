package com.bili.diushoujuaner.model.apihelper.request;

/**
 * Created by BiLi on 2016/4/27.
 */
public class BatchMemberAddReq {

    private long partyNo;
    private String members;

    public BatchMemberAddReq(long partyNo, String members) {
        this.partyNo = partyNo;
        this.members = members;
    }

    public long getPartyNo() {
        return partyNo;
    }

    public void setPartyNo(long partyNo) {
        this.partyNo = partyNo;
    }

    public String getMembers() {
        return members;
    }

    public void setMembers(String members) {
        this.members = members;
    }
}
