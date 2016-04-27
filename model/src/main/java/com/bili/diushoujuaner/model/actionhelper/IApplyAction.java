package com.bili.diushoujuaner.model.actionhelper;

import com.bili.diushoujuaner.model.actionhelper.respon.ActionResponse;
import com.bili.diushoujuaner.model.apihelper.request.FriendApplyReq;
import com.bili.diushoujuaner.model.apihelper.request.FriendAgreeReq;
import com.bili.diushoujuaner.model.apihelper.request.PartyApplyAgreeReq;
import com.bili.diushoujuaner.model.apihelper.request.PartyApplyReq;
import com.bili.diushoujuaner.model.callback.ActionStringCallbackListener;
import com.bili.diushoujuaner.utils.entity.vo.ApplyVo;

import java.util.List;

/**
 * Created by BiLi on 2016/4/24.
 */
public interface IApplyAction {

    void getAddUnReadCount(ActionStringCallbackListener<ActionResponse<Integer>> actionStringCallbackListener);

    void getFriendApply(FriendApplyReq friendApplyReq, ActionStringCallbackListener<ActionResponse<Void>> actionStringCallbackListener);

    void getPartyApply(PartyApplyReq partyApplyReq, ActionStringCallbackListener<ActionResponse<Void>> actionStringCallbackListener);

    void getApplyVoList(ActionStringCallbackListener<ActionResponse<List<ApplyVo>>> actionStringCallbackListener);

    void updateApplyRead(ActionStringCallbackListener<ActionResponse<Void>> actionStringCallbackListener);

    void getFriendApplyAgree(FriendAgreeReq friendAgreeReq, ActionStringCallbackListener<ActionResponse<Void>> actionStringCallbackListener);

    void getPartyApplyAgree(PartyApplyAgreeReq partyApplyAgreeReq, ActionStringCallbackListener<ActionResponse<Void>> actionStringCallbackListener);
}
