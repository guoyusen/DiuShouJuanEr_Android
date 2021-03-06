package com.bili.diushoujuaner.presenter.presenter;

/**
 * Created by BiLi on 2016/4/6.
 */
public interface ContentEditActivityPresenter {

    void publishNewAutograph(String autograph);

    void publishNewFeedBack(String feedBack);

    void publishNewMemberName(String memberName);

    void publishNewPartyName(String partyName);

    void publishNewPartyIntroduce(String introduce);

    void getFriendApply(long friendNo, String content);

    void getPartyApply(long partyNo, String content);

    void getFriendRemarkUpdate(long friendNo, String remark);

}
