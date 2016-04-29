package com.bili.diushoujuaner.presenter.presenter;

/**
 * Created by BiLi on 2016/4/24.
 */
public interface ContactAddActivityPresenter {

    void getApplyVoList();

    void getFriendApplyAgree(long friendNo);

    void getPartyApplyAgree(long partyNo, long memberNo);

    void getFriendApply(long friendNo, String content);

}
