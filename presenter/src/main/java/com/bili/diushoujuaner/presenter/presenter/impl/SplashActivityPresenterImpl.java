package com.bili.diushoujuaner.presenter.presenter.impl;

import android.content.Context;

import com.bili.diushoujuaner.model.actionhelper.action.CustomSessionAction;
import com.bili.diushoujuaner.presenter.base.BasePresenter;
import com.bili.diushoujuaner.presenter.presenter.SplashActivityPresenter;
import com.bili.diushoujuaner.presenter.view.ISplashView;
import com.bili.diushoujuaner.utils.Constant;

/**
 * Created by BiLi on 2016/3/11.
 */
public class SplashActivityPresenterImpl extends BasePresenter<ISplashView> implements SplashActivityPresenter {

    public SplashActivityPresenterImpl(ISplashView baseView, Context context) {
        super(baseView, context);
    }

    @Override
    public void getNextActivity() {
        if(CustomSessionAction.getInstance(context).getIsLogined()){
            if(isBindViewValid()){
                getBindView().showNextActivity(Constant.SHOW_TYPE_MAIN);
            }
        }else{
            if(isBindViewValid()){
                getBindView().showNextActivity(Constant.SHOW_TYPE_LOGIN);
            }
        }
    }
}
