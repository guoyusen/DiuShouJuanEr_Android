package com.bili.diushoujuaner.presenter.view;

import com.bili.diushoujuaner.model.apihelper.response.RecallDto;
import com.bili.diushoujuaner.presenter.base.IBaseView;
import com.bili.diushoujuaner.utils.entity.FriendVo;

import java.util.List;

/**
 * Created by BiLi on 2016/3/13.
 */
public interface IContactDetailView extends IBaseView {

    void showContact(FriendVo friendVo);

    void showRecent(RecallDto recallDto);

}
