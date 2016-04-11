package com.bili.diushoujuaner.presenter.presenter;

import com.bili.diushoujuaner.model.apihelper.request.UserInfoReq;

/**
 * Created by BiLi on 2016/4/2.
 */
public interface UserActivityPresenter {

    void getUserInfo();

    void updateUserInfo(UserInfoReq userInfoReq);

    void updateHeadPic(String path);

}
