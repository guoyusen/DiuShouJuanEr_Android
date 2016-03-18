package com.bili.diushoujuaner.presenter.presenter;

import android.content.Context;

import com.bili.diushoujuaner.model.action.UserInfoAction;
import com.bili.diushoujuaner.model.apihelper.ApiRespon;
import com.bili.diushoujuaner.model.callback.ActionCallbackListener;
import com.bili.diushoujuaner.model.databasehelper.DBManager;
import com.bili.diushoujuaner.model.databasehelper.DataTypeUtil;
import com.bili.diushoujuaner.model.databasehelper.dao.User;
import com.bili.diushoujuaner.model.preferhelper.CustomSessionPreference;
import com.bili.diushoujuaner.presenter.base.BasePresenter;
import com.bili.diushoujuaner.presenter.base.IBaseView;
import com.bili.diushoujuaner.presenter.viewinterface.MainActivityView;
import com.bili.diushoujuaner.utils.Common;
import com.bili.diushoujuaner.utils.response.CustomSession;
import com.bili.diushoujuaner.utils.response.UserRes;

/**
 * Created by BiLi on 2016/3/13.
 */
public class MainActivityPresenter extends BasePresenter {

    public MainActivityPresenter(IBaseView baseView, Context context) {
        super(baseView, context);
    }

    public void getUserInfo(){
        getViewByClass(MainActivityView.class).getUserInfo(getUserFromDb());
        if(Common.checkNetworkStatus(context)){
            UserInfoAction.getInstance(context).getUserInfo(new ActionCallbackListener<ApiRespon<UserRes>>() {
                @Override
                public void onSuccess(ApiRespon<UserRes> result) {
                    if(showMessage(result.getRetCode(), result.getMessage())){
                        DBManager.getInstance().saveUser(result.getData());
                        getViewByClass(MainActivityView.class).getUserInfo(getUserFromDb());
                    }
                }

                @Override
                public void onFailure(int errorCode) {
                    showError(errorCode);
                }
            });
        }
    }

    private User getUserFromDb(){
        CustomSession customSession = CustomSessionPreference.getInstance().getCustomSession();
        return DBManager.getInstance().getUser(customSession.getUserNo());
    }
}
