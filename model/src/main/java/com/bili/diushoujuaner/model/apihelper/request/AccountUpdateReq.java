package com.bili.diushoujuaner.model.apihelper.request;

/**
 * Created by BiLi on 2016/4/9.
 */
public class AccountUpdateReq {

    private String mobile;
    private String password;
    private String code;

    public AccountUpdateReq(String mobile, String password, String code) {
        this.mobile = mobile;
        this.password = password;
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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
