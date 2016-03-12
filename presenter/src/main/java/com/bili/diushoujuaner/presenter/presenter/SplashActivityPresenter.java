package com.bili.diushoujuaner.presenter.presenter;

import android.content.Context;

import com.bili.diushoujuaner.model.action.CustomSessionAction;
import com.bili.diushoujuaner.model.callback.ActionCallbackListener;
import com.bili.diushoujuaner.presenter.base.IBaseView;
import com.bili.diushoujuaner.presenter.base.BasePresenter;
import com.bili.diushoujuaner.presenter.viewinterface.SplashActivityView;
import com.bili.diushoujuaner.utils.Constant;

/**
 * Created by BiLi on 2016/3/11.
 */
public class SplashActivityPresenter extends BasePresenter {

    public SplashActivityPresenter(IBaseView baseView, Context context) {
        super(baseView, context);
    }

    public void getNextActivity(){
        CustomSessionAction.getInstance(context).getAccessToken(new ActionCallbackListener<Boolean>() {
            @Override
            public void onSuccess(Boolean isLogined) {
                if(!isLogined){
                    getViewByClass(SplashActivityView.class).showNextActivity(Constant.SHOW_TYPE_LOGIN);
                }else{
                    getViewByClass(SplashActivityView.class).showNextActivity(Constant.SHOW_TYPE_MAIN);
                }
            }

            @Override
            public void onFailure(int errorCode) {

            }
        });
    }

}
