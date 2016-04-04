package com.bili.diushoujuaner.activity;

import com.bili.diushoujuaner.R;
import com.bili.diushoujuaner.base.BaseActivity;

/**
 * Created by BiLi on 2016/4/3.
 */
public class NoticeActivity extends BaseActivity {

    @Override
    public void initView() {
        setContentView(R.layout.activity_notice);
    }

    @Override
    public void setViewStatus() {
        showPageHead("系统通知",null,null);
    }
}
