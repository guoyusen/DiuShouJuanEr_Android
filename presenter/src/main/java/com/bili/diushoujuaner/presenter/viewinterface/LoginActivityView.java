package com.bili.diushoujuaner.presenter.viewinterface;

import com.bili.diushoujuaner.presenter.base.IBaseView;

/**
 * Created by BiLi on 2016/3/10.
 */
public interface LoginActivityView extends IBaseView{

    void LoginSuccess();

    void LoginFail(String errorMsg);

}
