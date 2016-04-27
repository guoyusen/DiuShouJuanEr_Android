package com.bili.diushoujuaner.model.apihelper.request;

/**
 * Created by BiLi on 2016/3/11.
 */
public class UserAccountReq {

    private String mobile;
    private String password;

    public UserAccountReq(String mobile, String password) {
        this.mobile = mobile;
        this.password = password;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
