package com.bili.diushoujuaner.presenter.view;

import com.bili.diushoujuaner.model.apihelper.response.RecallDto;
import com.bili.diushoujuaner.presenter.base.IBaseView;
import com.bili.diushoujuaner.utils.entity.FriendVo;

import java.util.List;

/**
 * Created by BiLi on 2016/4/9.
 */
public interface ISpaceView extends IBaseView{

    void showContactInfo(FriendVo friendVo);

    void showRecallList(List<RecallDto> recallDtoList);

    void showMoreRecallList(List<RecallDto> recallDtoList);

    void setLoadMoreEnd();

    void removeRecallByPosition(int position);

    void updateWallPaper(String wallPaper);

}
