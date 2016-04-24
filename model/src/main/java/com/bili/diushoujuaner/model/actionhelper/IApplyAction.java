package com.bili.diushoujuaner.model.actionhelper;

import com.bili.diushoujuaner.model.actionhelper.respon.ActionRespon;
import com.bili.diushoujuaner.model.apihelper.request.FriendAddReq;
import com.bili.diushoujuaner.model.apihelper.request.FriendAgreeReq;
import com.bili.diushoujuaner.model.callback.ActionStringCallbackListener;
import com.bili.diushoujuaner.utils.entity.vo.ApplyVo;

import java.util.List;

/**
 * Created by BiLi on 2016/4/24.
 */
public interface IApplyAction {

    void getAddUnReadCount(ActionStringCallbackListener<ActionRespon<Integer>> actionStringCallbackListener);

    void getFriendAdd(FriendAddReq friendAddReq, ActionStringCallbackListener<ActionRespon<Void>> actionStringCallbackListener);

    void getApplyVoList(ActionStringCallbackListener<ActionRespon<List<ApplyVo>>> actionStringCallbackListener);

    void updateApplyRead(ActionStringCallbackListener<ActionRespon<Void>> actionStringCallbackListener);

    void getFriendAgree(FriendAgreeReq friendAgreeReq, ActionStringCallbackListener<ActionRespon<Void>> actionStringCallbackListener);
}
