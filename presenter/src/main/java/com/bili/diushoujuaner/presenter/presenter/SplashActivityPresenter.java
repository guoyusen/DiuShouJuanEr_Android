package com.bili.diushoujuaner.presenter.presenter;

import android.content.Context;

import com.bili.diushoujuaner.model.action.CustomSessionAction;
import com.bili.diushoujuaner.model.action.respon.ActionRespon;
import com.bili.diushoujuaner.model.callback.ActionCallbackListener;
import com.bili.diushoujuaner.presenter.base.BasePresenter;
import com.bili.diushoujuaner.presenter.view.ISplashView;
import com.bili.diushoujuaner.utils.Constant;

/**
 * Created by BiLi on 2016/3/11.
 */
public class SplashActivityPresenter extends BasePresenter<ISplashView> {

    public SplashActivityPresenter(ISplashView baseView, Context context) {
        super(baseView, context);
    }

    public void getNextActivity(){
        CustomSessionAction.getInstance().getIsLogined(new ActionCallbackListener<ActionRespon<Boolean>>() {
            @Override
            public void onSuccess(ActionRespon<Boolean> result) {
                if (showMessage(result.getRetCode(), result.getMessage())) {
                    if(result.getData()){
                        getRelativeView().showNextActivity(Constant.SHOW_TYPE_MAIN);
                    }else{
                        getRelativeView().showNextActivity(Constant.SHOW_TYPE_LOGIN);
                    }
                }
            }

            @Override
            public void onFailure(int errorCode) {
            }
        });
    }

}
