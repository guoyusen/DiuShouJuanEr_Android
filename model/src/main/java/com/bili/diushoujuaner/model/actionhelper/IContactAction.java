package com.bili.diushoujuaner.model.actionhelper;

import com.bili.diushoujuaner.model.actionhelper.respon.ActionRespon;
import com.bili.diushoujuaner.model.apihelper.request.ContactInfoReq;
import com.bili.diushoujuaner.model.apihelper.request.ContactsSearchReq;
import com.bili.diushoujuaner.model.apihelper.request.MemberNameUpdateReq;
import com.bili.diushoujuaner.model.apihelper.request.PartyIntroduceUpdateReq;
import com.bili.diushoujuaner.model.apihelper.request.PartyNameUpdateReq;
import com.bili.diushoujuaner.model.callback.ActionStringCallbackListener;
import com.bili.diushoujuaner.utils.entity.dto.ContactDto;
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

    void getMemberNameUpdate(MemberNameUpdateReq memberNameUpdateReq, ActionStringCallbackListener<ActionRespon<String>> actionStringCallbackListener);

    void getPartyNameUpdate(PartyNameUpdateReq partyNameUpdateReq, ActionStringCallbackListener<ActionRespon<String>> actionStringCallbackListener);

    void getPartyIntroduceUpdate(PartyIntroduceUpdateReq partyIntroduceUpdateReq, ActionStringCallbackListener<ActionRespon<String>> actionStringCallbackListener);

    void getContactsSearch(ContactsSearchReq contactsSearchReq, ActionStringCallbackListener<ActionRespon<List<ContactDto>>> actionStringCallbackListener);
}
