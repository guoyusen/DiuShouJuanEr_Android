package com.bili.diushoujuaner.presenter.base;

import android.content.Context;

import com.bili.diushoujuaner.model.actionhelper.action.UserInfoAction;
import com.bili.diushoujuaner.model.actionhelper.respon.ActionRespon;
import com.bili.diushoujuaner.model.cachehelper.ACache;
import com.bili.diushoujuaner.model.callback.ActionStringCallbackListener;
import com.bili.diushoujuaner.model.preferhelper.CustomSessionPreference;
import com.bili.diushoujuaner.model.tempHelper.ChattingTemper;
import com.bili.diushoujuaner.model.tempHelper.ContactTemper;
import com.bili.diushoujuaner.model.tempHelper.GoodTemper;
import com.bili.diushoujuaner.model.tempHelper.RecallTemper;
import com.bili.diushoujuaner.presenter.messager.LocalClient;
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

    protected T getBindView(){
        return (T)baseView;
    }

    protected boolean isBindViewValid(){
        //每次调用getBindView()进行方法调用时，需要判断当前绑定view的状态，不可用，则不进行调用
        return this.baseView != null;
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
            case Constant.WARNING_FILE:
                baseView.showWarning(Constant.WARNING_FILE);
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

    public void getLogout(){
        showLoading(Constant.LOADING_TOP,"正在退出账号");
        UserInfoAction.getInstance(context).getLogout(new ActionStringCallbackListener<ActionRespon<Void>>() {
            @Override
            public void onSuccess(ActionRespon<Void> result) {
                hideLoading(Constant.LOADING_TOP);
                if(showMessage(result.getRetCode(), result.getMessage())){
                    LocalClient.getInstance(context).sendMessageToService(Constant.HANDLER_LOGOUT, null);
                    ACache.getInstance().remove(Constant.ACACHE_LAST_TIME_CONTACT);
                    ACache.getInstance().remove(Constant.ACACHE_USER_RECALL_PREFIX + CustomSessionPreference.getInstance().getCustomSession().getUserNo());
                    CustomSessionPreference.getInstance().clear();
                    UserInfoAction.getInstance(context).clearUser();
                    RecallTemper.getInstance().clear();
                    GoodTemper.getInstance().clear();
                    ContactTemper.getInstance().clear();
                    ChattingTemper.getInstance().clear();
                    if(isBindViewValid()){
                        getBindView().exitActivity();
                    }
                }
            }

            @Override
            public void onFailure(int errorCode) {
                showError(errorCode);
                hideLoading(Constant.LOADING_TOP);
            }
        });
    }
}
