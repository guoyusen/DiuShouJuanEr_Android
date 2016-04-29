package com.bili.diushoujuaner.model.eventhelper;

/**
 * Created by BiLi on 2016/4/28.
 */
public class UnGroupPartyEvent {

    private long partyNo;

    public UnGroupPartyEvent(long partyNo) {
        this.partyNo = partyNo;
    }

    public long getPartyNo() {
        return partyNo;
    }

    public void setPartyNo(long partyNo) {
        this.partyNo = partyNo;
    }
}
