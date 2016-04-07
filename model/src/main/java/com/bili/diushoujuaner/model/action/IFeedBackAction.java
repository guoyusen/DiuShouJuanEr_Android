package com.bili.diushoujuaner.model.action;

import com.bili.diushoujuaner.model.action.respon.ActionRespon;
import com.bili.diushoujuaner.model.apihelper.request.FeedBackReq;
import com.bili.diushoujuaner.model.callback.ActionCallbackListener;

/**
 * Created by BiLi on 2016/4/6.
 */
public interface IFeedBackAction {

    void getFeedBackAdd(FeedBackReq feedBackReq, final ActionCallbackListener<ActionRespon<Void>> actionCallbackListener);

}