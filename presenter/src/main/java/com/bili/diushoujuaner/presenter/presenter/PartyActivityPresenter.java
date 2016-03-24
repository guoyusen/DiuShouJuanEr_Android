package com.bili.diushoujuaner.presenter.presenter;

import android.content.Context;

import com.bili.diushoujuaner.model.databasehelper.DBManager;
import com.bili.diushoujuaner.presenter.base.BasePresenter;
import com.bili.diushoujuaner.presenter.view.IPartyView;
import com.bili.diushoujuaner.utils.Constant;
import com.bili.diushoujuaner.utils.entity.PartyVo;
import com.bili.diushoujuaner.utils.pinyin.PinyinComparator;
import com.bili.diushoujuaner.utils.pinyin.PinyinUtil;

import java.util.Collections;
import java.util.List;

/**
 * Created by BiLi on 2016/3/19.
 */
public class PartyActivityPresenter extends BasePresenter<IPartyView> {

    public PartyActivityPresenter(IPartyView baseView, Context context) {
        super(baseView, context);
    }

    public void getPartyList(){
        showLoading(Constant.LOADING_DEFAULT, "");

        List<PartyVo> partyVoList = DBManager.getInstance().getPartyVoList();
        Collections.sort(partyVoList, new PinyinComparator());
        if(partyVoList.size() == 1){
            char capital = PinyinUtil.getHeadCapitalByChar(partyVoList.get(0).getDisplayName().charAt(0));
            partyVoList.get(0).setSortLetter((capital >= 'A' && capital <= 'Z') ? (capital + "") : "#");
        }

        getRelativeView().showPartyList(partyVoList);
        hideLoading(Constant.LOADING_DEFAULT);
    }
}
