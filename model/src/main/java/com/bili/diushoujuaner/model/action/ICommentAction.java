package com.bili.diushoujuaner.model.action;

import com.bili.diushoujuaner.model.action.respon.ActionRespon;
import com.bili.diushoujuaner.model.apihelper.request.CommentAddReq;
import com.bili.diushoujuaner.model.apihelper.request.CommentRemoveReq;
import com.bili.diushoujuaner.model.apihelper.response.CommentDto;
import com.bili.diushoujuaner.model.callback.ActionCallbackListener;

/**
 * Created by BiLi on 2016/4/2.
 */
public interface ICommentAction {

    void getCommentAdd(CommentAddReq commentAddReq, final ActionCallbackListener<ActionRespon<CommentDto>> actionCallbackListener);

    void getCommentRemove(CommentRemoveReq commentRemoveReq, final ActionCallbackListener<ActionRespon<Long>> actionCallbackListener);
}
