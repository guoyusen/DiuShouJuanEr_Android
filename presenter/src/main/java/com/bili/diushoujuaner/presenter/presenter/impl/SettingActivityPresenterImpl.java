package com.bili.diushoujuaner.presenter.presenter.impl;

import android.content.Context;

import com.bili.diushoujuaner.model.preferhelper.SettingPreference;
import com.bili.diushoujuaner.presenter.base.BasePresenter;
import com.bili.diushoujuaner.presenter.presenter.SettingActivityPresenter;
import com.bili.diushoujuaner.presenter.view.ISettingView;
import com.bili.diushoujuaner.utils.entity.vo.SettingVo;

/**
 * Created by BiLi on 2016/4/7.
 */
public class SettingActivityPresenterImpl extends BasePresenter<ISettingView> implements SettingActivityPresenter {

    public SettingActivityPresenterImpl(ISettingView baseView, Context context) {
        super(baseView, context);
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
