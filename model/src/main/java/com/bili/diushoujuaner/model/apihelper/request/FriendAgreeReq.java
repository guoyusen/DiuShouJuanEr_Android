package com.bili.diushoujuaner.model.apihelper.request;

/**
 * Created by BiLi on 2016/4/24.
 */
public class FriendAgreeReq {

    private long friendNo;

    public FriendAgreeReq(long friendNo) {
        this.friendNo = friendNo;
    }

    public long getFriendNo() {
        return friendNo;
    }

    public void setFriendNo(long friendNo) {
        this.friendNo = friendNo;
    }
}
