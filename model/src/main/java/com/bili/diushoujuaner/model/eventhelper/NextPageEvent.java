package com.bili.diushoujuaner.model.eventhelper;

/**
 * Created by BiLi on 2016/4/8.
 */
public class NextPageEvent {

    private int pageIndex;
    private String mobile;

    public NextPageEvent(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public String getMobile() {
        return mobile;
    }

    public NextPageEvent setMobile(String mobile) {
        this.mobile = mobile;
        return this;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }
}
