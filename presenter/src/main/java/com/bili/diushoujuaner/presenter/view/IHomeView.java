package com.bili.diushoujuaner.presenter.view;

import com.bili.diushoujuaner.presenter.base.IBaseView;
import com.bili.diushoujuaner.utils.entity.dto.RecallDto;

import java.util.List;

/**
 * Created by BiLi on 2016/3/10.
 */
public interface IHomeView extends IBaseView {

    void showRecallList(List<RecallDto> recallDtoList);

    void showMoreRecallList(List<RecallDto> recallDtoList);

    void setRefreshingEnd();

    void setLoadMoreEnd();

}
