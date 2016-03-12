package com.bili.diushoujuaner.model.apihelper.api;

import com.bili.diushoujuaner.model.apihelper.callback.ApiCallbackListener;
import com.bili.diushoujuaner.utils.resquest.UserAccountDto;

/**
 * Created by BiLi on 2016/3/11.
 */
public interface Api {

    String HOST_ADDRESS = "http://192.168.137.1:8080/diushoujuaner/";
    String userLogin = HOST_ADDRESS + "1.0/users/login";

    void getUserLogin(UserAccountDto userAccountDto, ApiCallbackListener apiCallbackListener);

}
