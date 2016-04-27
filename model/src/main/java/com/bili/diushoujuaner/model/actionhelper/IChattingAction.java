package com.bili.diushoujuaner.model.actionhelper;

import com.bili.diushoujuaner.model.actionhelper.respon.ActionResponse;
import com.bili.diushoujuaner.model.callback.ActionStringCallbackListener;
import com.bili.diushoujuaner.utils.entity.vo.MessageVo;

import java.util.List;

/**
 * Created by BiLi on 2016/4/12.
 */
public interface IChattingAction {

    void getOffMessage(ActionStringCallbackListener<ActionResponse<List<MessageVo>>> actionStringCallbackListener);

    void getChattingList(ActionStringCallbackListener<ActionResponse<Void>> actionStringCallbackListener);

    void updateMessageRead(long userNo, int msgType);

    void deleteRecent(long userNo, int msgType);

}
