package com.bili.diushoujuaner.presenter.viewinterface;

import com.bili.diushoujuaner.model.databasehelper.dao.Friend;
import com.bili.diushoujuaner.presenter.base.IBaseView;

import java.util.List;

/**
 * Created by BiLi on 2016/3/13.
 */
public interface ContactFragmentView extends IBaseView {

    void getContactList(List<Friend> friendList);

}
