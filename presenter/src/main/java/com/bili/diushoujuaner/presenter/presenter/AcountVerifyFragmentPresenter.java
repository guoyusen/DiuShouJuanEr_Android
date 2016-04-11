package com.bili.diushoujuaner.presenter.presenter;

/**
 * Created by BiLi on 2016/4/8.
 */
public interface AcountVerifyFragmentPresenter {

    void getAcountRegister(String mobile, String password, String code);

    void getAcountReset(String mobile, String password, String code);

    void getVerifyCode(final String mobile, int type);

}
