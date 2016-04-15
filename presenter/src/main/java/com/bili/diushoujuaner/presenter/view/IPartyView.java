package com.bili.diushoujuaner.presenter.view;

import com.bili.diushoujuaner.presenter.base.IBaseView;
import com.bili.diushoujuaner.utils.entity.vo.PartyVo;

import java.util.List;

/**
 * Created by BiLi on 2016/3/19.
 */
public interface IPartyView extends IBaseView{

    void showPartyList(List<PartyVo> partyVoList);

}
