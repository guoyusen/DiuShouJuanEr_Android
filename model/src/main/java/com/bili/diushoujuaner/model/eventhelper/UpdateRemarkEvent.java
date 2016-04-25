package com.bili.diushoujuaner.model.eventhelper;

/**
 * Created by BiLi on 2016/4/25.
 */
public class UpdateRemarkEvent {

    private long friendNo;
    private String remark;

    public UpdateRemarkEvent(long friendNo, String remark) {
        this.friendNo = friendNo;
        this.remark = remark;
    }

    public long getFriendNo() {
        return friendNo;
    }

    public void setFriendNo(long friendNo) {
        this.friendNo = friendNo;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
