package com.bili.diushoujuaner.model.apihelper.request;

/**
 * Created by BiLi on 2016/4/25.
 */
public class FriendDeleteReq {

    private long friendNo;

    public FriendDeleteReq(long friendNo) {
        this.friendNo = friendNo;
    }

    public long getFriendNo() {
        return friendNo;
    }

    public void setFriendNo(long friendNo) {
        this.friendNo = friendNo;
    }
}
