package com.bili.diushoujuaner.presenter.viewinterface;

import com.bili.diushoujuaner.utils.response.RecallVo;
import com.bili.diushoujuaner.presenter.base.IBaseLoadingView;

import java.util.List;

/**
 * Created by BiLi on 2016/3/10.
 */
public interface HomeFragmentView extends IBaseLoadingView {

    void showRecallList(List<RecallVo> recallVoList);

    void showMoreRecallList(List<RecallVo> recallVoList);

}
