package com.bili.diushoujuaner.activity;


import com.bili.diushoujuaner.R;
import com.bili.diushoujuaner.base.BaseActivity;

public class LoginActivity extends BaseActivity {

    @Override
    public void tintStatusColor() {
        super.tintStatusColor();

        tintManager.setStatusBarTintResource(R.color.TRANSPARENT);
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_login);
    }

    @Override
    public void setViewStatus() {
        showPageHead(null, null, "新用户");
    }
}
