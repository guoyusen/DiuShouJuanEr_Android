package com.bili.diushoujuaner.model.apihelper.request;

/**
 * Created by BiLi on 2016/4/23.
 */
public class PartyIntroduceUpdateReq {

    private long partyNo;
    private String introduce;

    public PartyIntroduceUpdateReq(long partyNo, String introduce) {
        this.partyNo = partyNo;
        this.introduce = introduce;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public long getPartyNo() {
        return partyNo;
    }

    public void setPartyNo(long partyNo) {
        this.partyNo = partyNo;
    }
}
