package com.bili.diushoujuaner.model.action;

import com.bili.diushoujuaner.model.action.respon.ActionRespon;
import com.bili.diushoujuaner.model.callback.ActionCallbackListener;
import com.bili.diushoujuaner.utils.request.UserAccountReq;
import com.bili.diushoujuaner.utils.response.CustomSession;

/**
 * Created by BiLi on 2016/4/2.
 */
public interface ICustomSessionAction {

    boolean getIsLogined();

    void getUserLogin(UserAccountReq userAccountReq, final ActionCallbackListener<ActionRespon<CustomSession>> actionCallbackListener);

}
