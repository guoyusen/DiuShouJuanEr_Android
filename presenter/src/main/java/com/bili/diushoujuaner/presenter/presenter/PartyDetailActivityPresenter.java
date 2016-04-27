package com.bili.diushoujuaner.presenter.presenter;

import com.bili.diushoujuaner.utils.entity.vo.MemberVo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BiLi on 2016/4/22.
 */
public interface PartyDetailActivityPresenter {

    void getContactInfo();

    void getContactInfo(long partyNo);

    ArrayList<MemberVo> getMemberVoList();

    ArrayList<MemberVo> getMemberVoList(long partyNo);

    String getMemberName(long partyNo);

    void updatePartyHeadPic(long partyNo, String path);

    boolean isPartied(long partyNo);

    void getMemberExit();

    void getMemberExit(long partyNo);

}
