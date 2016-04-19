package com.bili.diushoujuaner.model.actionhelper;

import com.bili.diushoujuaner.model.actionhelper.respon.ActionRespon;
import com.bili.diushoujuaner.model.callback.ActionStringCallbackListener;
import com.bili.diushoujuaner.utils.entity.vo.MessageVo;

import java.util.List;

/**
 * Created by BiLi on 2016/4/14.
 */
public interface IMessageAction {

    void getMessageList(long lastId, int pageIndex, int pageSize, final ActionStringCallbackListener<ActionRespon<List<MessageVo>>> actionStringCallbackListener);

    void saveMessageVo(MessageVo messageVo, ActionStringCallbackListener<ActionRespon<MessageVo>> actionStringCallbackListener);

}
