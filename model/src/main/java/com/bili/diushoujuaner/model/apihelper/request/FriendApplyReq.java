package com.bili.diushoujuaner.model.apihelper.request;

/**
 * Created by BiLi on 2016/4/24.
 */
public class FriendApplyReq {

    private long friendNo;
    private String content;

    public FriendApplyReq(long friendNo, String content) {
        this.friendNo = friendNo;
        this.content = content;
    }

    public long getFriendNo() {
        return friendNo;
    }

    public void setFriendNo(long friendNo) {
        this.friendNo = friendNo;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
