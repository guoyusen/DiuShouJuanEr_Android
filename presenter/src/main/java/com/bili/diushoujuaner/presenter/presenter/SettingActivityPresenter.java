package com.bili.diushoujuaner.presenter.presenter;

import com.bili.diushoujuaner.utils.entity.vo.SettingVo;

/**
 * Created by BiLi on 2016/4/7.
 */
public interface SettingActivityPresenter {

    void getSettings();

    void saveSettings(SettingVo settingVo);

}
