package com.bili.diushoujuaner.model.apihelper.request;

/**
 * Created by BiLi on 2016/4/25.
 */
public class RemarkUpdateReq {

    private String remark;
    private long friendNo;

    public RemarkUpdateReq(String remark, long friendNo) {
        this.remark = remark;
        this.friendNo = friendNo;
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
