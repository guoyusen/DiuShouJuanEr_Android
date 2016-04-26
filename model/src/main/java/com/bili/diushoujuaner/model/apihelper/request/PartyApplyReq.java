package com.bili.diushoujuaner.model.apihelper.request;

/**
 * Created by BiLi on 2016/4/25.
 */
public class PartyApplyReq {

    private long partyNo;
    private String content;

    public PartyApplyReq(long partyNo, String content) {
        this.partyNo = partyNo;
        this.content = content;
    }

    public long getPartyNo() {
        return partyNo;
    }

    public void setPartyNo(long partyNo) {
        this.partyNo = partyNo;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
