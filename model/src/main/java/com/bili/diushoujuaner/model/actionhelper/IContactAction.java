package com.bili.diushoujuaner.model.actionhelper;

import com.bili.diushoujuaner.model.actionhelper.respon.ActionResponse;
import com.bili.diushoujuaner.model.apihelper.request.ContactAddInfoReq;
import com.bili.diushoujuaner.model.apihelper.request.ContactsSearchReq;
import com.bili.diushoujuaner.model.apihelper.request.FriendDeleteReq;
import com.bili.diushoujuaner.model.apihelper.request.MemberExitReq;
import com.bili.diushoujuaner.model.apihelper.request.MemberForceExitReq;
import com.bili.diushoujuaner.model.apihelper.request.MemberNameUpdateReq;
import com.bili.diushoujuaner.model.apihelper.request.PartyAddReq;
import com.bili.diushoujuaner.model.apihelper.request.PartyIntroduceUpdateReq;
import com.bili.diushoujuaner.model.apihelper.request.PartyNameUpdateReq;
import com.bili.diushoujuaner.model.apihelper.request.RemarkUpdateReq;
import com.bili.diushoujuaner.model.callback.ActionFileCallbackListener;
import com.bili.diushoujuaner.model.callback.ActionStringCallbackListener;
import com.bili.diushoujuaner.utils.entity.dto.ContactDto;
import com.bili.diushoujuaner.utils.entity.vo.FriendVo;
import com.bili.diushoujuaner.utils.entity.vo.MemberVo;
import com.bili.diushoujuaner.utils.entity.vo.PartyVo;

import java.util.List;

/**
 * Created by BiLi on 2016/4/2.
 */
public interface IContactAction {

    List<PartyVo> getPartyVoList();

    void getContactList(boolean addToContactTemper, ActionStringCallbackListener<ActionResponse<List<FriendVo>>> actionStringCallbackListener);

    void getContactFromLocal(long userNo, ActionStringCallbackListener<ActionResponse<FriendVo>> actionStringCallbackListener);

    void getContactFromApi(ContactAddInfoReq contactAddInfoReq, ActionStringCallbackListener<ActionResponse<FriendVo>> actionStringCallbackListener);

    void getMemberNameUpdate(MemberNameUpdateReq memberNameUpdateReq, ActionStringCallbackListener<ActionResponse<String>> actionStringCallbackListener);

    void getPartyNameUpdate(PartyNameUpdateReq partyNameUpdateReq, ActionStringCallbackListener<ActionResponse<String>> actionStringCallbackListener);

    void getPartyIntroduceUpdate(PartyIntroduceUpdateReq partyIntroduceUpdateReq, ActionStringCallbackListener<ActionResponse<String>> actionStringCallbackListener);

    void getContactsSearch(ContactsSearchReq contactsSearchReq, ActionStringCallbackListener<ActionResponse<List<ContactDto>>> actionStringCallbackListener);

    void getAddContact(long userNo, int type);

    void getFriendDelete(FriendDeleteReq friendDeleteReq, ActionStringCallbackListener<ActionResponse<Void>> actionStringCallbackListener);

    void getFriendRemarkUpdate(RemarkUpdateReq remarkUpdateReq, ActionStringCallbackListener<ActionResponse<Void>> actionStringCallbackListener);

    void getPartyAdd(PartyAddReq partyAddReq, String path, ActionFileCallbackListener<ActionResponse<Void>> actionFileCallbackListener);

    void getMemberVoListFromLocal(long partyNo, ActionStringCallbackListener<ActionResponse<List<MemberVo>>> actionStringCallbackListener);

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

    void getMemberExit(MemberExitReq memberExitReq, ActionStringCallbackListener<ActionResponse<Void>> actionStringCallbackListener);

    void getMemberForceExit(MemberForceExitReq memberForceExitReq, ActionStringCallbackListener<ActionResponse<Void>> actionStringCallbackListener);
}
