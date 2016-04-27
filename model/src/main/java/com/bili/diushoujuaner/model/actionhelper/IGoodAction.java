package com.bili.diushoujuaner.model.actionhelper;

import com.bili.diushoujuaner.model.actionhelper.respon.ActionResponse;
import com.bili.diushoujuaner.model.callback.ActionStringCallbackListener;

/**
 * Created by BiLi on 2016/4/2.
 */
public interface IGoodAction {

    void getGoodAdd(long recallNo, ActionStringCallbackListener<ActionResponse<String>> actionStringCallbackListener);

    void getGoodRemove(long recallNo, ActionStringCallbackListener<ActionResponse<String>> actionStringCallbackListener);
}
