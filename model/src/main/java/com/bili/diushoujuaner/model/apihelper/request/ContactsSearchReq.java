package com.bili.diushoujuaner.model.apihelper.request;

/**
 * Created by BiLi on 2016/4/23.
 */
public class ContactsSearchReq {

    private String paramNo;

    public ContactsSearchReq(String paramNo) {
        this.paramNo = paramNo;
    }

    public String getParamNo() {
        return paramNo;
    }

    public void setParamNo(String paramNo) {
        this.paramNo = paramNo;
    }
}
