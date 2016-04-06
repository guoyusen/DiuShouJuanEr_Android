package com.bili.diushoujuaner.model.action;

import com.bili.diushoujuaner.model.action.respon.ActionRespon;
import com.bili.diushoujuaner.model.callback.ActionCallbackListener;

/**
 * Created by BiLi on 2016/4/2.
 */
public interface IGoodAction {

    void getGoodAdd(long recallNo, final ActionCallbackListener<ActionRespon<String>> actionCallbackListener);

    void getGoodRemove(long recallNo, final ActionCallbackListener<ActionRespon<String>> actionCallbackListener);
}
