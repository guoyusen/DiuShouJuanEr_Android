package com.bili.diushoujuaner.activity;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bili.diushoujuaner.R;
import com.bili.diushoujuaner.base.BaseActivity;
import com.bili.diushoujuaner.presenter.presenter.SettingActivityPresenter;
import com.bili.diushoujuaner.presenter.presenter.impl.SettingActivityPresenterImpl;
import com.bili.diushoujuaner.presenter.view.ISettingView;
import com.bili.diushoujuaner.utils.entity.vo.SettingVo;
import com.bili.diushoujuaner.widget.ShSwitchView;
import com.bili.diushoujuaner.widget.TintedBitmapDrawable;

import butterknife.Bind;

/**
 * Created by BiLi on 2016/4/3.
 */
public class SettingActivity extends BaseActivity<SettingActivityPresenter> implements View.OnClickListener, ISettingView {

    @Bind(R.id.switchVoice)
    ShSwitchView switchVoice;
    @Bind(R.id.switchFriend)
    ShSwitchView switchFriend;
    @Bind(R.id.switchVibrate)
    ShSwitchView switchVibrate;
    @Bind(R.id.switchGPRS)
    ShSwitchView switchGPRS;
    @Bind(R.id.ivArrowAbout)
    ImageView ivArrowAbout;
    @Bind(R.id.btnExit)
    Button btnExit;
    @Bind(R.id.layoutAbout)
    RelativeLayout layoutAbout;

    private SettingVo settingVo;

    @Override
    public void beforeInitView() {
        basePresenter = new SettingActivityPresenterImpl(this, context);
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_setting);
    }

    @Override
    public void setViewStatus() {
        showPageHead("设置", null, null);
        ivArrowAbout.setImageDrawable(new TintedBitmapDrawable(getResources(), R.mipmap.icon_arrow_right, ContextCompat.getColor(context, R.color.COLOR_8A8A8A)));

        btnExit.setOnClickListener(this);
        layoutAbout.setOnClickListener(this);

        getBindPresenter().getSettings();

        switchVibrate.setOnSwitchStateChangeListener(new ShSwitchView.OnSwitchStateChangeListener() {
            @Override
            public void onSwitchStateChange(boolean isOn) {
                settingVo.setIsMessageVibrate(isOn);
                getBindPresenter().saveSettings(settingVo);
            }
        });
        switchVoice.setOnSwitchStateChangeListener(new ShSwitchView.OnSwitchStateChangeListener() {
            @Override
            public void onSwitchStateChange(boolean isOn) {
                settingVo.setIsMessageVoice(isOn);
                getBindPresenter().saveSettings(settingVo);
            }
        });
        switchGPRS.setOnSwitchStateChangeListener(new ShSwitchView.OnSwitchStateChangeListener() {
            @Override
            public void onSwitchStateChange(boolean isOn) {
                settingVo.setIsImageGprs(isOn);
                getBindPresenter().saveSettings(settingVo);
            }
        });
        switchFriend.setOnSwitchStateChangeListener(new ShSwitchView.OnSwitchStateChangeListener() {
            @Override
            public void onSwitchStateChange(boolean isOn) {
                settingVo.setIsHomeFriend(isOn);
                getBindPresenter().saveSettings(settingVo);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnExit:
                basePresenter.getLogout();
                break;
            case R.id.layoutAbout:
                startActivity(new Intent(this, AboutActivity.class));
                break;
        }
    }

    @Override
    public void showSettings(SettingVo settingVo) {
        this.settingVo = settingVo;
        switchVoice.setOn(settingVo.isMessageVoice());
        switchGPRS.setOn(settingVo.isImageGprs());
        switchFriend.setOn(settingVo.isHomeFriend());
        switchVibrate.setOn(settingVo.isMessageVibrate());
    }
}
