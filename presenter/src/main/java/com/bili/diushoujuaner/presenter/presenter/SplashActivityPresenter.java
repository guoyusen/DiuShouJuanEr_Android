package com.bili.diushoujuaner.presenter.presenter;

import android.content.Context;

import com.bili.diushoujuaner.model.preferhelper.CustomSessionPreference;
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
        if(!CustomSessionPreference.getInstance().isLogined()){
            getRelativeView().showNextActivity(Constant.SHOW_TYPE_LOGIN);
        }else{
            getRelativeView().showNextActivity(Constant.SHOW_TYPE_MAIN);
        }
    }

}
