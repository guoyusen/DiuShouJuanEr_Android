package com.bili.diushoujuaner.model.apihelper.request;

/**
 * Created by BiLi on 2016/4/23.
 */
public class PartyHeadUpdateReq {

    private long partyNo;

    public PartyHeadUpdateReq(long partyNo) {
        this.partyNo = partyNo;
    }

    public long getPartyNo() {
        return partyNo;
    }

    public void setPartyNo(long partyNo) {
        this.partyNo = partyNo;
    }
}
