package com.bili.diushoujuaner.model.actionhelper;

import com.bili.diushoujuaner.model.actionhelper.respon.ActionResponse;
import com.bili.diushoujuaner.model.apihelper.request.RecallPublishReq;
import com.bili.diushoujuaner.model.apihelper.request.RecallRemoveReq;
import com.bili.diushoujuaner.model.apihelper.request.RecentRecallReq;
import com.bili.diushoujuaner.model.callback.ActionStringCallbackListener;
import com.bili.diushoujuaner.model.apihelper.request.RecallListReq;
import com.bili.diushoujuaner.utils.entity.dto.RecallDto;

import java.util.List;

/**
 * Created by BiLi on 2016/4/2.
 */
public interface IRecallAction {

    void getRecallListFromACache(ActionStringCallbackListener<ActionResponse<List<RecallDto>>> actionStringCallbackListener);

    void getUserRecallListFromACache(long userNo, ActionStringCallbackListener<ActionResponse<List<RecallDto>>> actionStringCallbackListener);

    void getRecallList(RecallListReq recallListReq, ActionStringCallbackListener<ActionResponse<List<RecallDto>>> actionStringCallbackListener);

    void getRecentRecall(RecentRecallReq recentRecallReq, ActionStringCallbackListener<ActionResponse<RecallDto>> actionStringCallbackListener);

    void getRecallRemove(RecallRemoveReq recallRemoveReq, ActionStringCallbackListener<ActionResponse<Long>> actionStringCallbackListener);

    void getRecallPublish(RecallPublishReq recallPublishReq, ActionStringCallbackListener<ActionResponse<RecallDto>> actionStringCallbackListener);

}
