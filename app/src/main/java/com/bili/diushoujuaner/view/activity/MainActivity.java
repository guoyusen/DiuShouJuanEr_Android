package com.bili.diushoujuaner.view.activity;


import com.bili.diushoujuaner.R;
import com.bili.diushoujuaner.view.base.BaseActivity;

public class MainActivity extends BaseActivity {

    @Override
    public void tintStatusColor() {
        super.tintStatusColor();
        tintManager.setStatusBarTintResource(R.color.TRANSPARENT);
    }

    @Override
    public void beforeInitView() {
        super.beforeInitView();
    }

    @Override
    public void initView() {
        super.initView();
        setContentView(R.layout.activity_main);
    }

}
