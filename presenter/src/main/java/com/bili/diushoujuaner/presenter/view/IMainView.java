package com.bili.diushoujuaner.presenter.view;

import com.bili.diushoujuaner.model.databasehelper.dao.User;
import com.bili.diushoujuaner.presenter.base.IBaseView;

/**
 * Created by BiLi on 2016/3/13.
 */
public interface IMainView extends IBaseView {

    void showUserInfo(User user);

}
