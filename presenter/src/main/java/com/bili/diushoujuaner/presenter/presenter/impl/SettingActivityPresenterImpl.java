package com.bili.diushoujuaner.presenter.presenter.impl;

import android.content.Context;

import com.bili.diushoujuaner.model.actionhelper.action.UserInfoAction;
import com.bili.diushoujuaner.model.actionhelper.respon.ActionRespon;
import com.bili.diushoujuaner.model.cachehelper.ACache;
import com.bili.diushoujuaner.model.callback.ActionStringCallbackListener;
import com.bili.diushoujuaner.model.preferhelper.CustomSessionPreference;
import com.bili.diushoujuaner.model.preferhelper.SettingPreference;
import com.bili.diushoujuaner.model.tempHelper.ChattingTemper;
import com.bili.diushoujuaner.model.tempHelper.ContactTemper;
import com.bili.diushoujuaner.model.tempHelper.GoodTemper;
import com.bili.diushoujuaner.model.tempHelper.RecallTemper;
import com.bili.diushoujuaner.presenter.base.BasePresenter;
import com.bili.diushoujuaner.presenter.presenter.SettingActivityPresenter;
import com.bili.diushoujuaner.presenter.view.ISettingView;
import com.bili.diushoujuaner.utils.Constant;
import com.bili.diushoujuaner.utils.entity.vo.SettingVo;

/**
 * Created by BiLi on 2016/4/7.
 */
public class SettingActivityPresenterImpl extends BasePresenter<ISettingView> implements SettingActivityPresenter {

    public SettingActivityPresenterImpl(ISettingView baseView, Context context) {
        super(baseView, context);
    }

    @Override
    public void getLogout() {
        showLoading(Constant.LOADING_TOP,"正在退出账号");
        UserInfoAction.getInstance(context).getLogout(new ActionStringCallbackListener<ActionRespon<Void>>() {
            @Override
            public void onSuccess(ActionRespon<Void> result) {
                hideLoading(Constant.LOADING_TOP);
                if(showMessage(result.getRetCode(), result.getMessage())){
                    ACache.getInstance().remove(Constant.ACACHE_LAST_TIME_CONTACT);
                    ACache.getInstance().remove(Constant.ACACHE_USER_RECALL_PREFIX + CustomSessionPreference.getInstance().getCustomSession().getUserNo());
                    CustomSessionPreference.getInstance().clear();
                    UserInfoAction.getInstance(context).clearUser();
                    RecallTemper.clear();
                    GoodTemper.clear();
                    ContactTemper.clear();
                    ChattingTemper.clear();
                    if(isBindViewValid()){
                        getBindView().exitActivity();
                    }
                }
            }

            @Override
            public void onFailure(int errorCode) {
                showError(errorCode);
                hideLoading(Constant.LOADING_TOP);
            }
        });
    }

    @Override
    public void getSettings() {
        if(isBindViewValid()){
            getBindView().showSettings(SettingPreference.getInstance().getSetting());
        }
    }

    @Override
    public void saveSettings(SettingVo settingVo) {
        SettingPreference.getInstance().saveSettings(settingVo);
    }
}
