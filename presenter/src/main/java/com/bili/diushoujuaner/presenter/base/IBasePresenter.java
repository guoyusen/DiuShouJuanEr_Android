package com.bili.diushoujuaner.presenter.base;

/**
 * Created by BiLi on 2016/3/10.
 */
public interface IBasePresenter<V> {

    void attachView(V view);

    void detachView();

}
