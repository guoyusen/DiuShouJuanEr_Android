package com.bili.diushoujuaner.presenter.viewinterface;

import com.bili.diushoujuaner.presenter.base.IBaseLoadingView;

/**
 * Created by BiLi on 2016/3/10.
 */
public interface LoginActivityView extends IBaseLoadingView{

    void loginSuccess();

    void loginFail(String errorMsg);

}
