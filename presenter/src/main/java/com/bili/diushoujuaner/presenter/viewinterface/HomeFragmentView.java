package com.bili.diushoujuaner.presenter.viewinterface;

import com.bili.diushoujuaner.model.entities.RecallVo;
import com.bili.diushoujuaner.presenter.base.IBaseView;

import java.util.List;

/**
 * Created by BiLi on 2016/3/10.
 */
public interface HomeFragmentView extends IBaseView {

    void showRecallList(List<RecallVo> recallVoList);

    void showMoreRecallList(List<RecallVo> recallVoList);

}
