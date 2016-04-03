package com.bili.diushoujuaner.model.action;

import com.bili.diushoujuaner.model.action.respon.ActionRespon;
import com.bili.diushoujuaner.model.callback.ActionCallbackListener;
import com.bili.diushoujuaner.model.apihelper.request.UserAccountReq;
import com.bili.diushoujuaner.model.apihelper.response.CustomSession;

/**
 * Created by BiLi on 2016/4/2.
 */
public interface ICustomSessionAction {

    boolean getIsLogined();

    void getUserLogin(UserAccountReq userAccountReq, final ActionCallbackListener<ActionRespon<CustomSession>> actionCallbackListener);

}
