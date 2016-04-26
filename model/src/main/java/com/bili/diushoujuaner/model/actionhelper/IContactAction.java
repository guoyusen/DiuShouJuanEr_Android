package com.bili.diushoujuaner.model.actionhelper;

import com.bili.diushoujuaner.model.actionhelper.respon.ActionRespon;
import com.bili.diushoujuaner.model.apihelper.request.ContactAddInfoReq;
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

    void getContactFromApi(ContactAddInfoReq contactAddInfoReq, ActionStringCallbackListener<ActionRespon<FriendVo>> actionStringCallbackListener);

    void getMemberNameUpdate(MemberNameUpdateReq memberNameUpdateReq, ActionStringCallbackListener<ActionRespon<String>> actionStringCallbackListener);

    void getPartyNameUpdate(PartyNameUpdateReq partyNameUpdateReq, ActionStringCallbackListener<ActionRespon<String>> actionStringCallbackListener);

    void getPartyIntroduceUpdate(PartyIntroduceUpdateReq partyIntroduceUpdateReq, ActionStringCallbackListener<ActionRespon<String>> actionStringCallbackListener);

    void getContactsSearch(ContactsSearchReq contactsSearchReq, ActionStringCallbackListener<ActionRespon<List<ContactDto>>> actionStringCallbackListener);

    void getAddContact(long userNo, int type);

    void getFriendDelete(FriendDeleteReq friendDeleteReq, ActionStringCallbackListener<ActionRespon<Void>> actionStringCallbackListener);

    void getFriendRemarkUpdate(RemarkUpdateReq remarkUpdateReq, ActionStringCallbackListener<ActionRespon<Void>> actionStringCallbackListener);

    void getPartyAdd(PartyAddReq partyAddReq, String path, ActionFileCallbackListener<ActionRespon<Void>> actionFileCallbackListener);

    /**
     * 添加群成功，对于自己而言，需要获取整个群的信息，含成员
     * 获取信息后通知更新界面
     */
    void getWholePartyInfo(long partyNo, long memberNo, String time);

    /**
     * 被通知有人加入了群，这时候自己 已经加入了群，只需要增量更新一个新成员的信息
     * 获取信息后通知更新界面
     * @param partyNo
     * @param memberNo
     */
    void getSingleMemberInfo(long partyNo, long memberNo, String time);
}
