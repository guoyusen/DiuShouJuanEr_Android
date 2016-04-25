package com.bili.diushoujuaner.model.apihelper.request;

/**
 * Created by BiLi on 2016/4/25.
 */
public class PartyAddReq {

    private String partyName;

    public PartyAddReq(String partyName) {
        this.partyName = partyName;
    }

    public String getPartyName() {
        return partyName;
    }

    public void setPartyName(String partyName) {
        this.partyName = partyName;
    }
}
