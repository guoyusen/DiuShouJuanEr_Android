package com.bili.diushoujuaner.presenter.presenter;

import com.bili.diushoujuaner.utils.entity.vo.MemberVo;

import java.util.List;

/**
 * Created by BiLi on 2016/4/22.
 */
public interface PartyDetailActivityPresenter {

    void getContactInfo();

    void getContactInfo(long partyNo);

    List<MemberVo> getMemberVoList();

    List<MemberVo> getMemberVoList(long partyNo);

    String getMemberName();

    long getUserNo();

    void updatePartyHeadPic(long partyNo, String path);

    boolean isPartied(long partyNo);

    void getMemberExit();

    void getMemberExit(long partyNo);

}
