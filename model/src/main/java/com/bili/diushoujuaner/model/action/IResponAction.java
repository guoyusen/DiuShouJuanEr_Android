package com.bili.diushoujuaner.model.action;

import com.bili.diushoujuaner.model.action.respon.ActionRespon;
import com.bili.diushoujuaner.model.apihelper.request.ResponAddReq;
import com.bili.diushoujuaner.model.apihelper.request.ResponRemoveReq;
import com.bili.diushoujuaner.model.apihelper.response.ResponDto;
import com.bili.diushoujuaner.model.callback.ActionStringCallbackListener;

/**
 * Created by BiLi on 2016/4/2.
 */
public interface IResponAction {

    void getResponAdd(ResponAddReq responAddReq, final ActionStringCallbackListener<ActionRespon<ResponDto>> actionStringCallbackListener);

    void getResponRemove(ResponRemoveReq responRemoveReq, final ActionStringCallbackListener<ActionRespon<Long>> actionStringCallbackListener);
}
