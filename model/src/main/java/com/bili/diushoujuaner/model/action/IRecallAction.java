package com.bili.diushoujuaner.model.action;

import com.bili.diushoujuaner.model.action.respon.ActionRespon;
import com.bili.diushoujuaner.model.apihelper.request.RecentRecallReq;
import com.bili.diushoujuaner.model.callback.ActionCallbackListener;
import com.bili.diushoujuaner.model.apihelper.request.RecallListReq;
import com.bili.diushoujuaner.model.apihelper.response.RecallDto;

import java.util.List;

/**
 * Created by BiLi on 2016/4/2.
 */
public interface IRecallAction {

    List<RecallDto> getRecallListFromACache();

    void getRecallList(RecallListReq recallListReq, final ActionCallbackListener<ActionRespon<List<RecallDto>>> actionCallbackListener);

    void getRecentRecall(RecentRecallReq recentRecallReq, final ActionCallbackListener<ActionRespon<RecallDto>> actionCallbackListener);

}
