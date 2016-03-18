package com.bili.diushoujuaner.presenter.viewinterface;

import com.bili.diushoujuaner.presenter.base.IBaseView;
import com.bili.diushoujuaner.utils.entity.FriendVo;

import java.util.List;

/**
 * Created by BiLi on 2016/3/13.
 */
public interface ContactFragmentView extends IBaseView {

    void showContactList(List<FriendVo> friendVoList);

}
