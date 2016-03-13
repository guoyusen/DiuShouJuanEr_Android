package com.bili.diushoujuaner.activity;


import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bili.diushoujuaner.R;
import com.bili.diushoujuaner.base.BaseFragmentActivity;
import com.bili.diushoujuaner.callback.IShowMainMenu;
import com.bili.diushoujuaner.fragment.ContactFragment;
import com.bili.diushoujuaner.fragment.HomeFragment;
import com.bili.diushoujuaner.fragment.MessageFragment;
import com.bili.diushoujuaner.model.databasehelper.dao.User;
import com.bili.diushoujuaner.presenter.presenter.MainActivityPresenter;
import com.bili.diushoujuaner.presenter.viewinterface.MainActivityView;
import com.bili.diushoujuaner.utils.Common;
import com.bili.diushoujuaner.utils.Imageloader;
import com.bili.diushoujuaner.utils.manager.ActivityManager;
import com.bili.diushoujuaner.widget.CustomViewPager;
import com.bili.diushoujuaner.widget.badgeview.BGABadgeRadioButton;
import com.bili.diushoujuaner.widget.badgeview.BGABadgeTextView;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;

public class MainActivity extends BaseFragmentActivity implements View.OnClickListener, IShowMainMenu, MainActivityView {

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
    @Bind(R.id.btnMenuExit)
    Button btnMenuExit;
    @Bind(R.id.textSystemNotice)
    BGABadgeTextView textSystemNotice;
    @Bind(R.id.textAutograph)
    TextView textAutograph;
    @Bind(R.id.textUserName)
    TextView textUserName;

    private List<Fragment> fragmentList;
    private boolean isWaitingExit = false;
    private HomeFragment homeFragment;
    private MessageFragment messageFragment;
    private ContactFragment contactFragment;

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
        textAutograph.setOnClickListener(this);
        btnMainHome.setOnClickListener(this);
        btnMainMess.setOnClickListener(this);
        btnMainCont.setOnClickListener(this);

        homeFragment = HomeFragment.instantiation(0);
        messageFragment = MessageFragment.instantiation(1);
        contactFragment = ContactFragment.instantiation(2);
        homeFragment.setShowMainMenu(this);
        messageFragment.setShowMainMenu(this);
        contactFragment.setShowMainMenu(this);

        fragmentList.add(homeFragment);
        fragmentList.add(messageFragment);
        fragmentList.add(contactFragment);

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

        basePresenter = new MainActivityPresenter(this, getApplicationContext());
        getPresenterByClass(MainActivityPresenter.class).getUserInfo();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnMainHome:
                customViewPager.setCurrentItem(0, false);
                btnMainHome.setChecked(true);
                btnMainMess.setChecked(false);
                btnMainCont.setChecked(false);
                break;
            case R.id.btnMainMess:
                customViewPager.setCurrentItem(1, false);
                btnMainHome.setChecked(false);
                btnMainMess.setChecked(true);
                btnMainCont.setChecked(false);
                break;
            case R.id.btnMainCont:
                customViewPager.setCurrentItem(2, false);
                btnMainHome.setChecked(false);
                btnMainMess.setChecked(false);
                btnMainCont.setChecked(true);
                break;
            case R.id.btnMenuExit:
                ActivityManager.getInstance().exit();
                break;
            case R.id.menuHead:
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                break;
            case R.id.textAutograph:
                startActivity(new Intent(MainActivity.this, ContentEditActivity.class));
                break;
            case R.id.textFeedBack:

                break;
        }

    }

    @Override
    public void showMainMenu() {
        drawerLayout.openDrawer(GravityCompat.START);
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

    @Override
    public void getUserInfo(User user) {
        Imageloader.getInstance().displayDraweeView(Common.getCompleteUrl(user.getPicPath()),menuHead);
        textAutograph.setText(user.getAutograph());
        textUserName.setText(user.getNickName());
    }

    @Override
    public void showLoading(int loadingType) {
    }

    @Override
    public void hideLoading(int loadingType) {
    }

    @Override
    public void showWarning(String message) {
    }
}
