package com.bili.diushoujuaner.presenter.viewinterface;

import com.bili.diushoujuaner.presenter.base.IBaseView;
import com.bili.diushoujuaner.utils.entity.PartyVo;

import java.util.List;

/**
 * Created by BiLi on 2016/3/19.
 */
public interface PartyActivityView extends IBaseView{

    void showPartyList(List<PartyVo> partyVoList);

}
