package com.bili.diushoujuaner.model.actionhelper;

import com.bili.diushoujuaner.model.actionhelper.respon.ActionRespon;
import com.bili.diushoujuaner.model.apihelper.request.ContactInfoReq;
import com.bili.diushoujuaner.model.callback.ActionStringCallbackListener;
import com.bili.diushoujuaner.utils.entity.vo.FriendVo;
import com.bili.diushoujuaner.utils.entity.vo.PartyVo;

import java.util.List;

/**
 * Created by BiLi on 2016/4/2.
 */
public interface IContactAction {

    List<PartyVo> getPartyVoList();

    void getContactList(final ActionStringCallbackListener<ActionRespon<List<FriendVo>>> actionStringCallbackListener);

    void getContactFromLocal(final long userNo, final ActionStringCallbackListener<ActionRespon<FriendVo>> actionStringCallbackListener);

    void getContactFromApi(ContactInfoReq contactInfoReq, final ActionStringCallbackListener<ActionRespon<FriendVo>> actionStringCallbackListener);

}
