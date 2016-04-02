package com.bili.diushoujuaner.model.action;

import com.bili.diushoujuaner.model.action.respon.ActionRespon;
import com.bili.diushoujuaner.model.callback.ActionCallbackListener;
import com.bili.diushoujuaner.model.databasehelper.dao.User;

/**
 * Created by BiLi on 2016/4/2.
 */
public interface IUserInfoAction {

    User getUserFromLocal();

    void getUserInfo(final ActionCallbackListener<ActionRespon<User>> actionCallbackListener);

}
