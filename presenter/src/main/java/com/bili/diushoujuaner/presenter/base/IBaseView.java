package com.bili.diushoujuaner.presenter.base;

/**
 * Created by BiLi on 2016/3/10.
 */
public interface IBaseView {

    void showLoading(int loadingType, String message);

    void hideLoading(int loadingType);

    void showWarning(String message);

    void showWarning(int warningType);

}
