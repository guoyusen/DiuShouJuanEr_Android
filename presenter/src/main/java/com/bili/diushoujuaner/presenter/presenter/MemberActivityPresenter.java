package com.bili.diushoujuaner.presenter.presenter;

/**
 * Created by BiLi on 2016/4/26.
 */
public interface MemberActivityPresenter {

    void getMemberList(long partyNo);

    void getMemberForceExit(long partyNo, long memberNo);

}
