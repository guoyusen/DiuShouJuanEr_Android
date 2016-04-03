package com.bili.diushoujuaner.activity;

import com.bili.diushoujuaner.R;
import com.bili.diushoujuaner.base.BaseActivity;

/**
 * Created by BiLi on 2016/4/3.
 */
public class SettingActivity extends BaseActivity {

    @Override
    public void initView() {
        setContentView(R.layout.activity_setting);
    }

    @Override
    public void setViewStatus() {
        showPageHead("设置",null,null);
    }
}
