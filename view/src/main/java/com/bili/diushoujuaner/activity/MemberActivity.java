package com.bili.diushoujuaner.activity;

import com.bili.diushoujuaner.R;
import com.bili.diushoujuaner.base.BaseActivity;
import com.bili.diushoujuaner.widget.CustomListView;

import butterknife.Bind;

/**
 * Created by BiLi on 2016/4/3.
 */
public class MemberActivity extends BaseActivity {

    @Bind(R.id.listViewMember)
    CustomListView listViewMember;

    @Override
    public void beforeInitView() {

    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_member);
    }

    @Override
    public void setViewStatus() {
        showPageHead("群成员", R.mipmap.icon_menu, null);
    }

}
