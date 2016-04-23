package com.bili.diushoujuaner.presenter.presenter;

import com.bili.diushoujuaner.model.eventhelper.UpdateMessageEvent;

/**
 * Created by BiLi on 2016/4/14.
 */
public interface MessageActivityPresenter {

    void getContactInfo();

    void getMessageList();

    long getOwnerNo();

    void saveMessageVo(String content, int conType);

    boolean validateUpdateEvent(UpdateMessageEvent updateMessageEvent);

    void clearCurrentChat();

    void resetMessageSearchParam(long rowId, int pageIndex);

    void getNextActivity();

}
