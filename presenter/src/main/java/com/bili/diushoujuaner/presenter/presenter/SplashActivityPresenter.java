package com.bili.diushoujuaner.presenter.presenter;

import android.content.Context;

import com.bili.diushoujuaner.model.AccessTokenModel;
import com.bili.diushoujuaner.model.callback.ActionCallbackListener;
import com.bili.diushoujuaner.presenter.base.IBaseView;
import com.bili.diushoujuaner.utils.response.AccessTokenDto;
import com.bili.diushoujuaner.presenter.base.BasePresenter;
import com.bili.diushoujuaner.presenter.viewinterface.SplashActivityView;
import com.bili.diushoujuaner.model.preference.AccessTokenPreference;
import com.bili.diushoujuaner.utils.Constant;

/**
 * Created by BiLi on 2016/3/11.
 */
public class SplashActivityPresenter extends BasePresenter {

    public SplashActivityPresenter(IBaseView baseView, Context context) {
        super(baseView, context);
    }

    public void getNextActivity(){
        AccessTokenModel.getInstance(context).getAccessToken(new ActionCallbackListener<AccessTokenDto>() {
            @Override
            public void onSuccess(AccessTokenDto accessTokenDto) {
                if(accessTokenDto.getAccessToken().trim().length() <= 0){
                    getViewByClass(SplashActivityView.class).showNextActivity(Constant.SHOW_TYPE_LOGIN);
                }else{
                    getViewByClass(SplashActivityView.class).showNextActivity(Constant.SHOW_TYPE_MAIN);
                }
            }
            @Override
            public void onFailure(int errorCode) {}
        });
    }

}
