package com.bili.diushoujuaner.presenter.view;

import com.bili.diushoujuaner.presenter.base.IBaseView;
import com.bili.diushoujuaner.utils.entity.vo.ChattingVo;

import java.util.List;

/**
 * Created by BiLi on 2016/3/13.
 */
public interface IChattingView extends IBaseView {

    void updateAdapter();

    void showChatting(List<ChattingVo> chattingVoList);

}
