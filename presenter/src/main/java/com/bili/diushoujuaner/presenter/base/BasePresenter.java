package com.bili.diushoujuaner.presenter.base;

import android.content.Context;

/**
 * Created by BiLi on 2016/3/10.
 */
public class BasePresenter {

    protected IBaseView baseView;
    protected Context context;

    public BasePresenter(IBaseView baseView, Context context){
        this.baseView = baseView;
        this.context = context;
    }

    public void detachView() {
        this.baseView = null;
    }

    protected <T> T getViewByClass(Class<T> t){
        return (T)baseView;
    }
}
