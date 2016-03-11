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

    protected void showError(int errorCode) {
        switch (errorCode) {
            case 401:
                baseView.showWarning("错误代码 E-H 401");
            case 403:
                baseView.showWarning("错误代码 E-H 403");
                break;
            case 500:
                baseView.showWarning("服务器维护中...");
                break;
            case 100:
                baseView.showWarning("网不好，好捉急...");
                break;
            default:
                break;
        }
    }

    public boolean showMessage(String retCode, String message) {
        switch (retCode) {
            case "fail":
                baseView.showWarning(message);
                return false;
            default:
                return true;
        }
    }
}
