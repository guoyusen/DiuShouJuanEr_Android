package com.bili.diushoujuaner.model.action;

import com.bili.diushoujuaner.model.action.respon.ActionRespon;
import com.bili.diushoujuaner.model.apihelper.request.ContactInfoReq;
import com.bili.diushoujuaner.model.callback.ActionCallbackListener;
import com.bili.diushoujuaner.model.databasehelper.dao.User;
import com.bili.diushoujuaner.utils.entity.FriendVo;
import com.bili.diushoujuaner.utils.entity.PartyVo;

import java.util.List;

/**
 * Created by BiLi on 2016/4/2.
 */
public interface IContactAction {

    List<PartyVo> getPartyVoList();

    void getContactList(final ActionCallbackListener<ActionRespon<List<FriendVo>>> actionCallbackListener);

    FriendVo getContactFromLocal(long userNo);

    void getContactFromApi(ContactInfoReq contactInfoReq, final ActionCallbackListener<ActionRespon<FriendVo>> actionCallbackListener);

}
