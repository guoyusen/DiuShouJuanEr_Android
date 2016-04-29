package com.bili.diushoujuaner.presenter.base;

import android.content.Context;

import com.bili.diushoujuaner.model.actionhelper.action.UserInfoAction;
import com.bili.diushoujuaner.model.actionhelper.respon.ActionResponse;
import com.bili.diushoujuaner.model.cachehelper.ACache;
import com.bili.diushoujuaner.model.callback.ActionStringCallbackListener;
import com.bili.diushoujuaner.model.preferhelper.CustomSessionPreference;
import com.bili.diushoujuaner.model.tempHelper.ChattingTemper;
import com.bili.diushoujuaner.model.tempHelper.ContactTemper;
import com.bili.diushoujuaner.model.tempHelper.GoodTemper;
import com.bili.diushoujuaner.model.tempHelper.RecallTemper;
import com.bili.diushoujuaner.model.messagehelper.LocalClient;
import com.bili.diushoujuaner.utils.ConstantUtil;
import com.bili.diushoujuaner.utils.entity.po.User;

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
                baseView.showWarning(ConstantUtil.WARNING_401);
            case 403:
                baseView.showWarning(ConstantUtil.WARNING_403);
                break;
            case 503:
                baseView.showWarning(ConstantUtil.WARNING_503);
                break;
            case 500:
                baseView.showWarning(ConstantUtil.WARNING_500);
                break;
            case 100:
                baseView.showWarning(ConstantUtil.WARNING_NET);
                break;
            case ConstantUtil.ERROR_PARSE:
                baseView.showWarning(ConstantUtil.WARNING_PARSE);
                break;
            case ConstantUtil.WARNING_FILE:
                baseView.showWarning(ConstantUtil.WARNING_FILE);
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
            case ConstantUtil.RETCODE_FAIL:
            case ConstantUtil.RETCODE_ERROR:
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

    public User getCurrentUserInfo(){
        return UserInfoAction.getInstance(context).getUserFromLocal();
    }

    public long getCurrentUserNo(){
        return CustomSessionPreference.getInstance().getCustomSession().getUserNo();
    }

    public void getLogout(){
        showLoading(ConstantUtil.LOADING_TOP,"正在退出账号");
        UserInfoAction.getInstance(context).getLogout(new ActionStringCallbackListener<ActionResponse<Void>>() {
            @Override
            public void onSuccess(ActionResponse<Void> result) {
                hideLoading(ConstantUtil.LOADING_TOP);
                if(showMessage(result.getRetCode(), result.getMessage())){
                    LocalClient.getInstance(context).sendMessageToService(ConstantUtil.HANDLER_LOGOUT, null);
                    ACache.getInstance().remove(ConstantUtil.ACACHE_LAST_TIME_CONTACT);
                    ACache.getInstance().remove(ConstantUtil.ACACHE_USER_RECALL_PREFIX + CustomSessionPreference.getInstance().getCustomSession().getUserNo());
                    CustomSessionPreference.getInstance().clear();
                    UserInfoAction.getInstance(context).clearUser();
                    RecallTemper.getInstance().clear();
                    GoodTemper.getInstance().clear();
                    ContactTemper.getInstance().clear();
                    ChattingTemper.getInstance().clear(true);
                    if(isBindViewValid()){
                        getBindView().exitActivity();
                    }
                }
            }

            @Override
            public void onFailure(int errorCode) {
                showError(errorCode);
                hideLoading(ConstantUtil.LOADING_TOP);
            }
        });
    }
}
