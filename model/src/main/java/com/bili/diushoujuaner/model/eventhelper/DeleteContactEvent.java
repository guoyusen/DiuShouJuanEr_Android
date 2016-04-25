package com.bili.diushoujuaner.model.eventhelper;

/**
 * Created by BiLi on 2016/4/25.
 */
public class DeleteContactEvent {

    private int type;
    private long contNo;

    public DeleteContactEvent(int type, long contNo) {
        this.type = type;
        this.contNo = contNo;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getContNo() {
        return contNo;
    }

    public void setContNo(long contNo) {
        this.contNo = contNo;
    }
}
