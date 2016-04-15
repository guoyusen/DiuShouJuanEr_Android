package com.bili.diushoujuaner.presenter.view;

import com.bili.diushoujuaner.utils.entity.dto.RecallDto;
import com.bili.diushoujuaner.presenter.base.IBaseView;
import com.bili.diushoujuaner.utils.entity.vo.FriendVo;

/**
 * Created by BiLi on 2016/3/13.
 */
public interface IContactDetailView extends IBaseView {

    void showContactInfo(FriendVo friendVo);

    void showRecent(RecallDto recallDto);

}
