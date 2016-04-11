package com.bili.diushoujuaner.model.apihelper.request;

/**
 * Created by BiLi on 2016/4/10.
 */
public class RecallRemoveReq {

    private long recallNo;

    public RecallRemoveReq(long recallNo) {
        this.recallNo = recallNo;
    }

    public long getRecallNo() {
        return recallNo;
    }

    public void setRecallNo(long recallNo) {
        this.recallNo = recallNo;
    }
}
