package com.bili.diushoujuaner.presenter.base;

import android.content.Context;

import com.bili.diushoujuaner.utils.Constant;

/**
 * Created by BiLi on 2016/3/10.
 */
public class BasePresenter<T extends IBaseView> {

    protected IBaseView baseView;
    protected Context context;

    public BasePresenter(T baseView, Context context){
        this.baseView = baseView;
        this.context = context.getApplicationContext();
    }

    public void detachView() {
        this.baseView = null;
    }

    protected T getRelativeView(){
        return (T)baseView;
    }

    protected void showError(int errorCode) {
        if(this.baseView == null){
            return;
        }
        switch (errorCode) {
            case 401:
                baseView.showWarning(Constant.WARNING_401);
            case 403:
                baseView.showWarning(Constant.WARNING_403);
                break;
            case 503:
                baseView.showWarning(Constant.WARNING_503);
                break;
            case 500:
                baseView.showWarning(Constant.WARNING_500);
                break;
            case 100:
                baseView.showWarning(Constant.WARNING_NET);
                break;
            case Constant.ERROR_PARSE:
                baseView.showWarning(Constant.WARNING_PARSE);
                break;
            default:
                break;
        }
    }

    public boolean showMessage(String retCode, String message) {
        if(this.baseView == null){
            return false;
        }
        switch (retCode) {
            case Constant.RETCODE_FAIL:
            case Constant.RETCODE_ERROR:
                baseView.showWarning(message);
                return false;
            default:
                return true;
        }
    }

    protected void showLoading(int loadingType, String message){
        if(this.baseView == null){
            return;
        }
        baseView.showLoading(loadingType, message);
    }

    protected void hideLoading(int loadingType){
        if(this.baseView == null){
            return;
        }
        baseView.hideLoading(loadingType);
    }

    protected void showWarning(String message){
        if(this.baseView == null){
            return;
        }
        baseView.showWarning(message);
    }
}
