package com.bili.diushoujuaner.presenter.view;

import com.bili.diushoujuaner.presenter.base.IBaseView;
import com.bili.diushoujuaner.utils.entity.SettingVo;

/**
 * Created by BiLi on 2016/4/7.
 */
public interface ISettingView extends IBaseView {

    void showSettings(SettingVo settingVo);

    void exitActivity();

}
