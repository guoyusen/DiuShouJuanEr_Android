package com.bili.diushoujuaner.presenter.presenter;

/**
 * Created by BiLi on 2016/4/8.
 */
public interface AccountVerifyFragmentPresenter {

    void getAccountRegister(String mobile, String password, String code);

    void getAccountReset(String mobile, String password, String code);

    void getVerifyCode(final String mobile, int type);

}
