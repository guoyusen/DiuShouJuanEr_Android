package com.bili.diushoujuaner.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bili.diushoujuaner.R;
import com.bili.diushoujuaner.base.BaseFragmentActivity;
import com.bili.diushoujuaner.fragment.main.ContactFragment;
import com.bili.diushoujuaner.fragment.main.HomeFragment;
import com.bili.diushoujuaner.fragment.main.MessageFragment;
import com.bili.diushoujuaner.manager.ActivityManager;
import com.bili.diushoujuaner.ui.CustomViewPager;
import com.bili.diushoujuaner.ui.badgeview.BGABadgeRadioButton;
import com.bili.diushoujuaner.ui.badgeview.BGABadgeTextView;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends BaseFragmentActivity implements View.OnClickListener {

    @Bind(R.id.ivNavHead)
    SimpleDraweeView ivNavHead;
    @Bind(R.id.textNavTitle)
    TextView textNavTitle;
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
    @Bind(R.id.btnRecallAdd)
    ImageButton btnRecallAdd;
    @Bind(R.id.textContactAdd)
    TextView textContactAdd;
    @Bind(R.id.btnMenuExit)
    Button btnMenuExit;
    @Bind(R.id.textSystemNotice)
    BGABadgeTextView textSystemNotice;

    private List<Fragment> fragmentList;
    private boolean isWaitingExit = false;

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
        btnMenuExit.setOnClickListener(this);

        textContactAdd.setOnClickListener(this);
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
        textSystemNotice.showTextBadge("2");
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnMainHome:
                customViewPager.setCurrentItem(0, false);
                textNavTitle.setText(R.string.main_nav_home);
                btnMainHome.setChecked(true);
                btnMainMess.setChecked(false);
                btnMainCont.setChecked(false);

                btnRecallAdd.setVisibility(View.VISIBLE);
                textContactAdd.setVisibility(View.INVISIBLE);
                break;
            case R.id.btnMainMess:
                customViewPager.setCurrentItem(1, false);
                textNavTitle.setText(R.string.main_nav_mess);
                btnMainHome.setChecked(false);
                btnMainMess.setChecked(true);
                btnMainCont.setChecked(false);

                btnRecallAdd.setVisibility(View.INVISIBLE);
                textContactAdd.setVisibility(View.INVISIBLE);
                break;
            case R.id.btnMainCont:
                customViewPager.setCurrentItem(2, false);
                textNavTitle.setText(R.string.main_nav_cont);
                btnMainHome.setChecked(false);
                btnMainMess.setChecked(false);
                btnMainCont.setChecked(true);

                btnRecallAdd.setVisibility(View.INVISIBLE);
                textContactAdd.setVisibility(View.VISIBLE);
                break;
            case R.id.ivNavHead:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.textContactAdd:
                startActivity(new Intent(MainActivity.this, ContactAddActivity.class));
                break;
            case R.id.btnMenuExit:
                ActivityManager.getInstance().exit();
                break;
            case R.id.menuHead:
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                break;
        }

    }

    @Override
    public boolean onKeyDown(int key, KeyEvent event) {
        switch (key) {
            case KeyEvent.KEYCODE_BACK:
                if (isWaitingExit) {
                    isWaitingExit = false;
                    ActivityManager.getInstance().exit();
                } else {
                    Toast.makeText(this, "再按一次退出!", Toast.LENGTH_SHORT).show();
                    isWaitingExit = true;
                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            isWaitingExit = false;
                        }
                    }, 3000);
                }
                break;
        }
        return true;
    }

}
