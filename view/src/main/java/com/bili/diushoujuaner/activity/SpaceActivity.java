package com.bili.diushoujuaner.activity;

import com.bili.diushoujuaner.R;
import com.bili.diushoujuaner.base.BaseActivity;

/**
 * Created by BiLi on 2016/4/3.
 */
public class SpaceActivity extends BaseActivity {

    @Override
    public void initView() {
        setContentView(R.layout.activity_space);
    }

    @Override
    public void setViewStatus() {
        showPageHead("个人空间",null,null);
    }
}
