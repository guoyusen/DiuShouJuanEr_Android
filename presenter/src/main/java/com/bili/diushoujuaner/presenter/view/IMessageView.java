package com.bili.diushoujuaner.presenter.view;

import com.bili.diushoujuaner.presenter.base.IBaseView;
import com.bili.diushoujuaner.utils.entity.vo.FriendVo;
import com.bili.diushoujuaner.utils.entity.vo.MessageVo;
import com.bili.diushoujuaner.utils.entity.vo.PartyVo;

import java.util.List;

/**
 * Created by BiLi on 2016/4/14.
 */
public interface IMessageView extends IBaseView {

    void showContactInfo(FriendVo friendVo);

    void showContactInfo(PartyVo partyVo);

    void showMessageList(List<MessageVo> messageVoList);

}
