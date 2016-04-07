package com.bili.diushoujuaner.presenter.presenter.impl;

import android.content.Context;

import com.bili.diushoujuaner.model.action.impl.UserInfoAction;
import com.bili.diushoujuaner.model.cachehelper.ACache;
import com.bili.diushoujuaner.model.preferhelper.CustomSessionPreference;
import com.bili.diushoujuaner.model.preferhelper.SettingPreference;
import com.bili.diushoujuaner.model.tempHelper.ContactTemper;
import com.bili.diushoujuaner.model.tempHelper.GoodTemper;
import com.bili.diushoujuaner.model.tempHelper.RecallTemper;
import com.bili.diushoujuaner.presenter.base.BasePresenter;
import com.bili.diushoujuaner.presenter.presenter.SettingActivityPresenter;
import com.bili.diushoujuaner.presenter.view.ISettingView;
import com.bili.diushoujuaner.utils.Constant;
import com.bili.diushoujuaner.utils.entity.SettingVo;

/**
 * Created by BiLi on 2016/4/7.
 */
public class SettingActivityPresenterImpl extends BasePresenter<ISettingView> implements SettingActivityPresenter {

    public SettingActivityPresenterImpl(ISettingView baseView, Context context) {
        super(baseView, context);
    }

    @Override
    public void clearCurrentUserData() {
        RecallTemper.clear();
        GoodTemper.clear();
        ContactTemper.clear();
        CustomSessionPreference.getInstance().clear();
        UserInfoAction.getInstance(context).clearUser();
        ACache.getInstance().put(Constant.ACACHE_LAST_TIME_CONTACT,"");
    }

    @Override
    public void getSettings() {
        if(isBindViewValid()){
            getBindView().showSettings(SettingPreference.getInstance().getSetting());
        }
    }

    @Override
    public void saveSettings(SettingVo settingVo) {
        SettingPreference.getInstance().saveSettings(settingVo);
    }
}
