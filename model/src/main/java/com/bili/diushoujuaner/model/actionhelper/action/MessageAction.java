package com.bili.diushoujuaner.model.actionhelper.action;

import android.content.Context;

import com.bili.diushoujuaner.model.actionhelper.IMessageAction;
import com.bili.diushoujuaner.model.actionhelper.respon.ActionRespon;
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
    public void getMessageList(final long lastId, final int pageIndex, final int pageSize, final ActionStringCallbackListener<ActionRespon<List<MessageVo>>> actionStringCallbackListener) {
        Tasks.executeInBackground(context, new BackgroundWork<ActionRespon<List<MessageVo>>>() {
            @Override
            public ActionRespon<List<MessageVo>> doInBackground() throws Exception {
                List<MessageVo> messageVoList = DBManager.getInstance().getMessageList(lastId, pageIndex, pageSize);
                return ActionRespon.getActionRespon(messageVoList);
            }
        }, new Completion<ActionRespon<List<MessageVo>>>() {
            @Override
            public void onSuccess(Context context, ActionRespon<List<MessageVo>> result) {
                actionStringCallbackListener.onSuccess(result);
            }

            @Override
            public void onError(Context context, Exception e) {
                actionStringCallbackListener.onSuccess(ActionRespon.<List<MessageVo>>getActionResponError());
            }
        });
    }

    @Override
    public void saveMessageVo(final MessageVo messageVo, final ActionStringCallbackListener<ActionRespon<MessageVo>> actionStringCallbackListener) {
        Tasks.executeInBackground(context, new BackgroundWork<ActionRespon<MessageVo>>() {
            @Override
            public ActionRespon<MessageVo> doInBackground() throws Exception {
                return ActionRespon.getActionRespon(DBManager.getInstance().saveMessage(messageVo));
            }
        }, new Completion<ActionRespon<MessageVo>>() {
            @Override
            public void onSuccess(Context context, ActionRespon<MessageVo> result) {
                actionStringCallbackListener.onSuccess(result);
            }

            @Override
            public void onError(Context context, Exception e) {
                actionStringCallbackListener.onSuccess(ActionRespon.<MessageVo>getActionResponError());
            }
        });
    }
}
