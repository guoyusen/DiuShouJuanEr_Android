package com.bili.diushoujuaner.activity;

import com.bili.diushoujuaner.R;
import com.bili.diushoujuaner.base.BaseActivity;

/**
 * Created by BiLi on 2016/3/7.
 */
public class ContactAddActivity extends BaseActivity {

    @Override
    public void initView() {
        setContentView(R.layout.activity_contact_add);
    }

    @Override
    public void setViewStatus() {
        showPageHead("添加", null, null);
    }
}
