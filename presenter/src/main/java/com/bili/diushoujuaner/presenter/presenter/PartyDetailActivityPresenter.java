package com.bili.diushoujuaner.presenter.presenter;

import com.bili.diushoujuaner.utils.entity.vo.MemberVo;

import java.util.List;

/**
 * Created by BiLi on 2016/4/22.
 */
public interface PartyDetailActivityPresenter {

    void getContactInfo();

    List<MemberVo> getMemberVoList();

    String getMemberName();

    long getUserNo();

    void updatePartyHeadPic(long partyNo, String path);

}
