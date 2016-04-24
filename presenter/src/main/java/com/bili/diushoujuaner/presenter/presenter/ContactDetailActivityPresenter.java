package com.bili.diushoujuaner.presenter.presenter;

/**
 * Created by BiLi on 2016/4/3.
 */
public interface ContactDetailActivityPresenter {

    void getFriendVo(long userNo);

    void getRecentRecall(long userNo);

    void setCurrentChatting(long userNo, int msgType);

    boolean isFriend(long userNo);

    boolean isOwner(long userNo);

}
