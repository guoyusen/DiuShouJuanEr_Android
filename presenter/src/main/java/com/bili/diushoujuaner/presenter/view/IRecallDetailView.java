package com.bili.diushoujuaner.presenter.view;

import com.bili.diushoujuaner.presenter.base.IBaseView;
import com.bili.diushoujuaner.model.apihelper.response.RecallDto;

/**
 * Created by BiLi on 2016/3/23.
 */
public interface IRecallDetailView extends IBaseView{

    void showRecallDetail(RecallDto recallDto);

    void showGooderDetail(Long userNo);

    void refreshComment();

    void setGoodStatus(boolean isGood);

}
