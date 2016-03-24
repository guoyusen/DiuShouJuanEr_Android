package com.bili.diushoujuaner.utils.request;

/**
 * Created by BiLi on 2016/3/21.
 */
public class RecallListReq {

    private int type;
    private int pageIndex;
    private int pageSize;
    private long userNo;
    private long lastRecall;

    public long getLastRecall() {
        return lastRecall;
    }

    public void setLastRecall(long lastRecall) {
        this.lastRecall = lastRecall;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public long getUserNo() {
        return userNo;
    }

    public void setUserNo(long userNo) {
        this.userNo = userNo;
    }
}
