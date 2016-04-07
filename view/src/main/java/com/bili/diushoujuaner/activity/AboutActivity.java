package com.bili.diushoujuaner.activity;

import com.bili.diushoujuaner.R;
import com.bili.diushoujuaner.base.BaseActivity;
import com.facebook.drawee.view.SimpleDraweeView;
import com.bili.diushoujuaner.utils.Common;

import butterknife.Bind;

/**
 * Created by BiLi on 2016/4/3.
 */
public class AboutActivity extends BaseActivity {

    @Bind(R.id.guoyusen)
    SimpleDraweeView guoyusen;

    @Override
    public void initView() {
        setContentView(R.layout.activity_about);
    }

    @Override
    public void setViewStatus() {
        showPageHead("关于", null, null);
        Common.displayDraweeView("images/guoyusen/guoyusen.jpg", guoyusen);
    }

}
