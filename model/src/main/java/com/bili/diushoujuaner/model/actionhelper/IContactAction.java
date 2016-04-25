package com.bili.diushoujuaner.model.actionhelper;

import com.bili.diushoujuaner.model.actionhelper.respon.ActionRespon;
import com.bili.diushoujuaner.model.apihelper.request.ContactInfoReq;
import com.bili.diushoujuaner.model.apihelper.request.ContactsSearchReq;
import com.bili.diushoujuaner.model.apihelper.request.FriendDeleteReq;
import com.bili.diushoujuaner.model.apihelper.request.MemberNameUpdateReq;
import com.bili.diushoujuaner.model.apihelper.request.PartyAddReq;
import com.bili.diushoujuaner.model.apihelper.request.PartyIntroduceUpdateReq;
import com.bili.diushoujuaner.model.apihelper.request.PartyNameUpdateReq;
import com.bili.diushoujuaner.model.apihelper.request.RemarkUpdateReq;
import com.bili.diushoujuaner.model.callback.ActionFileCallbackListener;
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

    void getContactList(ActionStringCallbackListener<ActionRespon<List<FriendVo>>> actionStringCallbackListener);

    void getContactFromLocal(long userNo, ActionStringCallbackListener<ActionRespon<FriendVo>> actionStringCallbackListener);

    void getContactFromApi(ContactInfoReq contactInfoReq, ActionStringCallbackListener<ActionRespon<FriendVo>> actionStringCallbackListener);

    void getMemberNameUpdate(MemberNameUpdateReq memberNameUpdateReq, ActionStringCallbackListener<ActionRespon<String>> actionStringCallbackListener);

    void getPartyNameUpdate(PartyNameUpdateReq partyNameUpdateReq, ActionStringCallbackListener<ActionRespon<String>> actionStringCallbackListener);

    void getPartyIntroduceUpdate(PartyIntroduceUpdateReq partyIntroduceUpdateReq, ActionStringCallbackListener<ActionRespon<String>> actionStringCallbackListener);

    void getContactsSearch(ContactsSearchReq contactsSearchReq, ActionStringCallbackListener<ActionRespon<List<ContactDto>>> actionStringCallbackListener);

    void getAddContact(long userNo, int type);

    void getFriendDelete(FriendDeleteReq friendDeleteReq, ActionStringCallbackListener<ActionRespon<Void>> actionStringCallbackListener);

    void getFriendRemarkUpdate(RemarkUpdateReq remarkUpdateReq, ActionStringCallbackListener<ActionRespon<Void>> actionStringCallbackListener);

    void getPartyAdd(PartyAddReq partyAddReq, String path, ActionFileCallbackListener<ActionRespon<Void>> actionFileCallbackListener);
}
