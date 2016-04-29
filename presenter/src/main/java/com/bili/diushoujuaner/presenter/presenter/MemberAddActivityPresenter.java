package com.bili.diushoujuaner.presenter.presenter;

import java.util.List;

/**
 * Created by BiLi on 2016/4/26.
 */
public interface MemberAddActivityPresenter {

    void getContactList();

    void getMemberVoList(long partyNo);

    void getMembersAddToParty(long partyNo, List<Long> members);

}
