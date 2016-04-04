package com.bili.diushoujuaner.activity;

import com.bili.diushoujuaner.R;
import com.bili.diushoujuaner.base.BaseActivity;

/**
 * Created by BiLi on 2016/3/9.
 */
public class ChattingActivity extends BaseActivity {

    @Override
    public void beforeInitView() {
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_chatting);
    }

    @Override
    public void setViewStatus() {
        showPageHead("三木同学",null, null);
    }
}
