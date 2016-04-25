package com.bili.diushoujuaner.model.actionhelper;

import com.bili.diushoujuaner.model.actionhelper.respon.ActionRespon;
import com.bili.diushoujuaner.model.apihelper.request.ResponAddReq;
import com.bili.diushoujuaner.model.apihelper.request.ResponRemoveReq;
import com.bili.diushoujuaner.utils.entity.dto.ResponDto;
import com.bili.diushoujuaner.model.callback.ActionStringCallbackListener;

/**
 * Created by BiLi on 2016/4/2.
 */
public interface IResponAction {

    void getResponAdd(ResponAddReq responAddReq, ActionStringCallbackListener<ActionRespon<ResponDto>> actionStringCallbackListener);

    void getResponRemove(ResponRemoveReq responRemoveReq, ActionStringCallbackListener<ActionRespon<Long>> actionStringCallbackListener);
}
