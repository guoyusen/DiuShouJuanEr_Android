package com.bili.diushoujuaner.activity;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.bili.diushoujuaner.R;
import com.bili.diushoujuaner.base.BaseFragmentActivity;
import com.bili.diushoujuaner.fragment.main.ContactFragment;
import com.bili.diushoujuaner.fragment.main.HomeFragment;
import com.bili.diushoujuaner.fragment.main.MessageFragment;
import com.bili.diushoujuaner.ui.badgeview.BGABadgeRadioButton;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class MainActivity extends BaseFragmentActivity implements View.OnClickListener{

    @Bind(R.id.ivNavHead)
    SimpleDraweeView ivNavHead;
    @Bind(R.id.tvNavTitle)
    TextView tvNavTitle;
    @Bind(R.id.vpMain)
    ViewPager vpMain;
    @Bind(R.id.menuHead)
    SimpleDraweeView menuHead;
    @Bind(R.id.btnMainHome)
    BGABadgeRadioButton btnMainHome;
    @Bind(R.id.btnMainMess)
    BGABadgeRadioButton btnMainMess;
    @Bind(R.id.btnMainCont)
    BGABadgeRadioButton btnMainCont;

    private List<Fragment> fragmentList;

    @Override
    public void tintStatusColor() {
        super.tintStatusColor();
        tintManager.setStatusBarTintResource(R.color.TRANSPARENT);
    }

    @Override
    public void beforeInitView() {
        fragmentList = new ArrayList<>();
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_main);
    }

    @Override
    public void setViewStatus() {

        menuHead.setOnClickListener(this);
        ivNavHead.setOnClickListener(this);
        btnMainHome.setOnClickListener(this);
        btnMainMess.setOnClickListener(this);
        btnMainCont.setOnClickListener(this);

        fragmentList.add(HomeFragment.instantiation(0));
        fragmentList.add(MessageFragment.instantiation(1));
        fragmentList.add(ContactFragment.instantiation(2));

        FragmentPagerAdapter fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragmentList.get(position);
            }

            @Override
            public int getCount() {
                return fragmentList.size();
            }
        };
        vpMain.setAdapter(fragmentPagerAdapter);
        vpMain.setOffscreenPageLimit(3);

        btnMainHome.performClick();

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btnMainHome:
                vpMain.setCurrentItem(0, false);
                tvNavTitle.setText(R.string.main_nav_home);
                btnMainHome.setChecked(true);
                btnMainMess.setChecked(false);
                btnMainCont.setChecked(false);
                break;
            case R.id.btnMainMess:
                vpMain.setCurrentItem(1, false);
                tvNavTitle.setText(R.string.main_nav_mess);
                btnMainHome.setChecked(false);
                btnMainMess.setChecked(true);
                btnMainCont.setChecked(false);
                break;
            case R.id.btnMainCont:
                vpMain.setCurrentItem(2, false);
                tvNavTitle.setText(R.string.main_nav_cont);
                btnMainHome.setChecked(false);
                btnMainMess.setChecked(false);
                btnMainCont.setChecked(true);
                break;
            case R.id.ivNavHead:break;
            case R.id.menuHead:break;
        }

    }
}
