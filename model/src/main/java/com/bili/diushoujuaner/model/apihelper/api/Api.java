package com.bili.diushoujuaner.model.apihelper.api;

import com.bili.diushoujuaner.model.apihelper.callback.ApiCallbackListener;
import com.bili.diushoujuaner.utils.Constant;
import com.bili.diushoujuaner.utils.request.UserAccountReq;

/**
 * Created by BiLi on 2016/3/11.
 */
public interface Api {

    String getUserLogin = Constant.HOST_ADDRESS + "1.0/users/login";
    String getUserInfo = Constant.HOST_ADDRESS + "1.0/users/info";

    void getUserLogin(UserAccountReq userAccountReq, ApiCallbackListener apiCallbackListener);

    void getUserInfo(ApiCallbackListener apiCallbackListener);

}
