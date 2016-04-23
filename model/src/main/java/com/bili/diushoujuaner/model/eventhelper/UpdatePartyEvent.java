package com.bili.diushoujuaner.model.eventhelper;

/**
 * Created by BiLi on 2016/4/23.
 */
public class UpdatePartyEvent {

    private long partyNo;
    private String content;
    private int type;

    public UpdatePartyEvent(long partyNo, String content, int type) {
        this.partyNo = partyNo;
        this.content = content;
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getPartyNo() {
        return partyNo;
    }

    public void setPartyNo(long partyNo) {
        this.partyNo = partyNo;
    }

}
