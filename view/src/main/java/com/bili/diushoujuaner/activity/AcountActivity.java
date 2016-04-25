package com.bili.diushoujuaner.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;

import com.bili.diushoujuaner.R;
import com.bili.diushoujuaner.base.BaseFragmentActivity;
import com.bili.diushoujuaner.fragment.AcountMobileFragment;
import com.bili.diushoujuaner.fragment.AcountVerifyFragment;
import com.bili.diushoujuaner.utils.ConstantUtil;
import com.bili.diushoujuaner.model.eventhelper.NextPageEvent;
import com.bili.diushoujuaner.model.eventhelper.StartTimerEvent;
import com.bili.diushoujuaner.widget.CustomViewPager;
import com.bili.diushoujuaner.widget.stepview.StepsView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by BiLi on 2016/2/29.
 */
public class AcountActivity extends BaseFragmentActivity {

    @Bind(R.id.stepsView)
    StepsView stepsView;
    @Bind(R.id.viewPager)
    CustomViewPager viewPager;

    public static final String TAG = "AcountActivity";
    private String[] stepsRegist = {"手机号","验证信息","注册成功"};
    private String[] stepsReset = {"手机号","验证信息","重置成功"};
    private List<Fragment> fragmentList;

    private int type;

    @Override
    public void initIntentParam(Intent intent) {
        type = intent.getIntExtra(TAG, 0);
    }

    @Override
    public void beforeInitView() {
        fragmentList = new ArrayList<>();
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_acount);
    }

    @Override
    public void setViewStatus() {
        showPageHead(type == ConstantUtil.ACOUNT_UPDATE_REGIST  ? "注册账号" : "忘记密码",null,null);

        fragmentList.add(AcountMobileFragment.instantiation(type));
        fragmentList.add(AcountVerifyFragment.instantiation(type));

        stepsView.setCompletedPosition(0)
                .setLabels(type == ConstantUtil.ACOUNT_UPDATE_REGIST ? stepsRegist : stepsReset)
                .setBarColorIndicator(ContextCompat.getColor(context, R.color.BG_ECECEC))
                .setProgressColorIndicator(ContextCompat.getColor(context, R.color.COLOR_THEME_MAIN))
                .setLabelColorIndicator(ContextCompat.getColor(context, R.color.COLOR_THEME_MAIN))
                .drawView();

        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragmentList.get(position);
            }

            @Override
            public int getCount() {
                return fragmentList.size();
            }
        });
        viewPager.setOffscreenPageLimit(2);
        viewPager.setCanScroll(false);
        viewPager.setCurrentItem(0);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNextPageEvent(NextPageEvent nextPageEvent){
        switch (nextPageEvent.getPageIndex()){
            case 1:
                stepsView.setCompletedPosition(1);
                viewPager.setCurrentItem(1);
                EventBus.getDefault().post(new StartTimerEvent());
                break;
            case 2:
                stepsView.setCompletedPosition(2);
                switch(type){
                    case ConstantUtil.ACOUNT_UPDATE_REGIST:
                        showWarning("账户注册成功");
                        break;
                    case ConstantUtil.ACOUNT_UPDATE_RESET:
                        showWarning("密码重置成功");
                        break;
                }
                startActivity(new Intent(this, MainActivity.class));
                break;
        }

    }

}
