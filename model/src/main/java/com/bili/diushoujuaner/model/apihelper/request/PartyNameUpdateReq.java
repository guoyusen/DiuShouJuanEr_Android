package com.bili.diushoujuaner.model.apihelper.request;

/**
 * Created by BiLi on 2016/4/23.
 */
public class PartyNameUpdateReq {

    private long partyNo;
    private String partyName;

    public PartyNameUpdateReq(long partyNo, String partyName) {
        this.partyNo = partyNo;
        this.partyName = partyName;
    }

    public long getPartyNo() {
        return partyNo;
    }

    public void setPartyNo(long partyNo) {
        this.partyNo = partyNo;
    }

    public String getPartyName() {
        return partyName;
    }

    public void setPartyName(String partyName) {
        this.partyName = partyName;
    }
}
