package com.bili.diushoujuaner.model.actionhelper.action;

import android.content.Context;

import com.bili.diushoujuaner.model.actionhelper.IMessageAction;
import com.bili.diushoujuaner.model.actionhelper.respon.ActionResponse;
import com.bili.diushoujuaner.model.callback.ActionStringCallbackListener;
import com.bili.diushoujuaner.model.databasehelper.DBManager;
import com.bili.diushoujuaner.utils.entity.vo.MessageVo;
import com.nanotasks.BackgroundWork;
import com.nanotasks.Completion;
import com.nanotasks.Tasks;

import java.util.List;

/**
 * Created by BiLi on 2016/4/14.
 */
public class MessageAction implements IMessageAction {

    private static MessageAction messageAction;
    private Context context;

    public MessageAction(Context context) {
        this.context = context;
    }

    public static synchronized MessageAction getInstance(Context context){
        if(messageAction == null){
            messageAction = new MessageAction(context);
        }
        return messageAction;
    }

    @Override
    public void getMessageList(final long lastId, final int pageIndex, final int pageSize, final ActionStringCallbackListener<ActionResponse<List<MessageVo>>> actionStringCallbackListener) {
        Tasks.executeInBackground(context, new BackgroundWork<ActionResponse<List<MessageVo>>>() {
            @Override
            public ActionResponse<List<MessageVo>> doInBackground() throws Exception {
                List<MessageVo> messageVoList = DBManager.getInstance().getMessageList(lastId, pageIndex, pageSize);
                return ActionResponse.getActionRespon(messageVoList);
            }
        }, new Completion<ActionResponse<List<MessageVo>>>() {
            @Override
            public void onSuccess(Context context, ActionResponse<List<MessageVo>> result) {
                actionStringCallbackListener.onSuccess(result);
            }

            @Override
            public void onError(Context context, Exception e) {
                actionStringCallbackListener.onSuccess(ActionResponse.<List<MessageVo>>getActionResponError());
            }
        });
    }

    @Override
    public void saveMessageVo(final MessageVo messageVo, final ActionStringCallbackListener<ActionResponse<MessageVo>> actionStringCallbackListener) {
        Tasks.executeInBackground(context, new BackgroundWork<ActionResponse<MessageVo>>() {
            @Override
            public ActionResponse<MessageVo> doInBackground() throws Exception {
                return ActionResponse.getActionRespon(DBManager.getInstance().saveMessage(messageVo));
            }
        }, new Completion<ActionResponse<MessageVo>>() {
            @Override
            public void onSuccess(Context context, ActionResponse<MessageVo> result) {
                actionStringCallbackListener.onSuccess(result);
            }

            @Override
            public void onError(Context context, Exception e) {
                actionStringCallbackListener.onSuccess(ActionResponse.<MessageVo>getActionResponError());
            }
        });
    }
}
