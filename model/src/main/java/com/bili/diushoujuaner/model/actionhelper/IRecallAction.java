package com.bili.diushoujuaner.model.actionhelper;

import com.bili.diushoujuaner.model.actionhelper.respon.ActionRespon;
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

    void getRecallListFromACache(final ActionStringCallbackListener<ActionRespon<List<RecallDto>>> actionStringCallbackListener);

    void getUserRecallListFromACache(long userNo, final ActionStringCallbackListener<ActionRespon<List<RecallDto>>> actionStringCallbackListener);

    void getRecallList(RecallListReq recallListReq, final ActionStringCallbackListener<ActionRespon<List<RecallDto>>> actionStringCallbackListener);

    void getRecentRecall(RecentRecallReq recentRecallReq, final ActionStringCallbackListener<ActionRespon<RecallDto>> actionStringCallbackListener);

    void getRecallRemove(RecallRemoveReq recallRemoveReq, final ActionStringCallbackListener<ActionRespon<Long>> actionStringCallbackListener);

    void getRecallPublish(RecallPublishReq recallPublishReq, final ActionStringCallbackListener<ActionRespon<RecallDto>> actionStringCallbackListener);

}
