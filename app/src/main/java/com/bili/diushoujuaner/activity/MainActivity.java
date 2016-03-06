package com.bili.diushoujuaner.activity;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.TextView;

import com.bili.diushoujuaner.R;
import com.bili.diushoujuaner.base.BaseFragmentActivity;
import com.bili.diushoujuaner.fragment.main.ContactFragment;
import com.bili.diushoujuaner.fragment.main.HomeFragment;
import com.bili.diushoujuaner.fragment.main.MessageFragment;
import com.bili.diushoujuaner.ui.CustomViewPager;
import com.bili.diushoujuaner.ui.badgeview.BGABadgeRadioButton;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends BaseFragmentActivity implements View.OnClickListener {

    @Bind(R.id.ivNavHead)
    SimpleDraweeView ivNavHead;
    @Bind(R.id.tvNavTitle)
    TextView tvNavTitle;
    @Bind(R.id.customViewPager)
    CustomViewPager customViewPager;
    @Bind(R.id.menuHead)
    SimpleDraweeView menuHead;
    @Bind(R.id.btnMainHome)
    BGABadgeRadioButton btnMainHome;
    @Bind(R.id.btnMainMess)
    BGABadgeRadioButton btnMainMess;
    @Bind(R.id.btnMainCont)
    BGABadgeRadioButton btnMainCont;
    @Bind(R.id.drawerLayout)
    DrawerLayout drawerLayout;

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
        customViewPager.setAdapter(fragmentPagerAdapter);
        customViewPager.setOffscreenPageLimit(3);

        btnMainHome.performClick();

        btnMainMess.showTextBadge("5");
        btnMainHome.showTextBadge("5");

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnMainHome:
                customViewPager.setCurrentItem(0, false);
                tvNavTitle.setText(R.string.main_nav_home);
                btnMainHome.setChecked(true);
                btnMainMess.setChecked(false);
                btnMainCont.setChecked(false);
                break;
            case R.id.btnMainMess:
                customViewPager.setCurrentItem(1, false);
                tvNavTitle.setText(R.string.main_nav_mess);
                btnMainHome.setChecked(false);
                btnMainMess.setChecked(true);
                btnMainCont.setChecked(false);
                break;
            case R.id.btnMainCont:
                customViewPager.setCurrentItem(2, false);
                tvNavTitle.setText(R.string.main_nav_cont);
                btnMainHome.setChecked(false);
                btnMainMess.setChecked(false);
                btnMainCont.setChecked(true);
                break;
            case R.id.ivNavHead:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.menuHead:
                break;
        }

    }

}
