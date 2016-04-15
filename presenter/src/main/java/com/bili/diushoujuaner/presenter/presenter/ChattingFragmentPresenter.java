package com.bili.diushoujuaner.presenter.presenter;

/**
 * Created by BiLi on 2016/4/12.
 */
public interface ChattingFragmentPresenter {

    void getOffMessage();

    void getChattingList();

    void setCurrentChatting(long userNo, int msgType);

}
