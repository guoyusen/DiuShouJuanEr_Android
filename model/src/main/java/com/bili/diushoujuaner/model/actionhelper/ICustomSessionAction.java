package com.bili.diushoujuaner.model.actionhelper;

import com.bili.diushoujuaner.model.actionhelper.respon.ActionRespon;
import com.bili.diushoujuaner.model.callback.ActionStringCallbackListener;
import com.bili.diushoujuaner.model.apihelper.request.UserAccountReq;

/**
 * Created by BiLi on 2016/4/2.
 */
public interface ICustomSessionAction {

    boolean getIsLogined();

    void getUserLogin(UserAccountReq userAccountReq, final ActionStringCallbackListener<ActionRespon<Void>> actionStringCallbackListener);

}
