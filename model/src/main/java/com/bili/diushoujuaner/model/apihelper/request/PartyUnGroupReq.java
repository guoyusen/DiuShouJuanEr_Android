package com.bili.diushoujuaner.model.apihelper.request;

/**
 * Created by BiLi on 2016/4/28.
 */
public class PartyUnGroupReq {

    private long partyNo;

    public PartyUnGroupReq(long partyNo) {
        this.partyNo = partyNo;
    }

    public long getPartyNo() {
        return partyNo;
    }

    public void setPartyNo(long partyNo) {
        this.partyNo = partyNo;
    }
}
