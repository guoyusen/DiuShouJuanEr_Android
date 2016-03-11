package com.bili.diushoujuaner.presenter.base;

/**
 * Created by BiLi on 2016/3/10.
 */
public class BasePresenter implements IBasePresenter<IBaseView> {

    protected IBaseView baseView;

    @Override
    public void attachView(IBaseView view) {
        this.baseView = view;
    }

    @Override
    public void detachView() {
        this.baseView = null;
    }

    protected <T> T getViewByClass(Class<T> t){
        return (T)baseView;
    }
}
