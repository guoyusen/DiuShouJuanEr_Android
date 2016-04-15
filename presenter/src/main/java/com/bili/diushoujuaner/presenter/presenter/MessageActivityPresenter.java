package com.bili.diushoujuaner.presenter.presenter;

/**
 * Created by BiLi on 2016/4/14.
 */
public interface MessageActivityPresenter {

    void getContactInfo();

    void getMessageList();

    void resetMessageSearchParam(long rowId, int pageIndex);

    long getOwnerNo();

}
