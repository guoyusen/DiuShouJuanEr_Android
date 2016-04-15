package com.bili.diushoujuaner.model.actionhelper.action;

import android.content.Context;

import com.bili.diushoujuaner.model.actionhelper.IChattingAction;
import com.bili.diushoujuaner.model.actionhelper.respon.ActionRespon;
import com.bili.diushoujuaner.model.apihelper.ApiRespon;
import com.bili.diushoujuaner.model.apihelper.api.ApiAction;
import com.bili.diushoujuaner.model.apihelper.callback.ApiStringCallbackListener;
import com.bili.diushoujuaner.model.callback.ActionStringCallbackListener;
import com.bili.diushoujuaner.model.databasehelper.DBManager;
import com.bili.diushoujuaner.model.tempHelper.ChattingTemper;
import com.bili.diushoujuaner.utils.Common;
import com.bili.diushoujuaner.utils.Constant;
import com.bili.diushoujuaner.utils.GsonParser;
import com.bili.diushoujuaner.utils.entity.dto.OffMsgDto;
import com.bili.diushoujuaner.utils.entity.vo.MessageVo;
import com.google.gson.reflect.TypeToken;
import com.nanotasks.BackgroundWork;
import com.nanotasks.Completion;
import com.nanotasks.Tasks;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BiLi on 2016/4/12.
 */
public class ChattingAction implements IChattingAction {

    private static ChattingAction chattingAction;
    private Context context;

    public ChattingAction(Context context) {
        this.context = context;
    }

    public static synchronized ChattingAction getInstance(Context context){
        if(chattingAction == null){
            chattingAction = new ChattingAction(context);
        }
        return chattingAction;
    }

    @Override
    public void getChattingList(final ActionStringCallbackListener<ActionRespon<Void>> actionStringCallbackListener) {
        Tasks.executeInBackground(context, new BackgroundWork<ActionRespon<Void>>() {
            @Override
            public ActionRespon<Void> doInBackground() throws Exception {
                DBManager.getInstance().getRecentMessage();
                return ActionRespon.getActionRespon(null);
            }
        }, new Completion<ActionRespon<Void>>() {
            @Override
            public void onSuccess(Context context, ActionRespon<Void> result) {
                actionStringCallbackListener.onSuccess(result);
            }

            @Override
            public void onError(Context context, Exception e) {
                actionStringCallbackListener.onSuccess(ActionRespon.<Void>getActionResponError());
            }
        });
    }

    @Override
    public void getOffMessage(final ActionStringCallbackListener<ActionRespon<List<MessageVo>>> actionStringCallbackListener) {
        ApiAction.getInstance().getOffMsg(new ApiStringCallbackListener() {
            @Override
            public void onSuccess(final String data) {
                Tasks.executeInBackground(context, new BackgroundWork<ActionRespon<List<MessageVo>>>() {
                    @Override
                    public ActionRespon<List<MessageVo>> doInBackground() throws Exception {
                        ApiRespon<List<OffMsgDto>> result = GsonParser.getInstance().fromJson(data, new TypeToken<ApiRespon<List<OffMsgDto>>>(){}.getType());
                        if(result.getIsLegal()){
                            return processOffMessage(result.getData());
                        }else{
                            return ActionRespon.getActionRespon(result.getMessage(), result.getRetCode(),null);
                        }

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
            public void onFailure(int errorCode) {
                actionStringCallbackListener.onFailure(errorCode);
            }
        });
    }

    private ActionRespon<List<MessageVo>> processOffMessage(List<OffMsgDto> messageList){
        //TODO 异步处理所有的消息，并返回消息界面需要的消息
        List<MessageVo> msgDtoList = new ArrayList<>();
        for(OffMsgDto item : messageList){
            switch(item.getMsgType()){
                case Constant.CHAT_FRI:
                case Constant.CHAT_PAR:
                    msgDtoList.add(processOffMessage(item));
                    break;
                case Constant.CHAT_GOOD:
                    //发送Event通知
                    break;
                case Constant.CHAT_TIME:
                    break;
            }
        }
        return ActionRespon.getActionRespon(msgDtoList);
    }

    private MessageVo processOffMessage(OffMsgDto item){
        MessageVo messageVo = Common.getMessageVoFromOffMsgVo(item);
        //正在聊天界面
        //聊天类型相同
        //群聊或者单聊
        if(ChattingTemper.isChatting() && ChattingTemper.getMsgType() == messageVo.getMsgType() &&
                ((ChattingTemper.getMsgType() == Constant.CHAT_PAR && ChattingTemper.getUserNo() == messageVo.getToNo()) ||
                        ChattingTemper.getMsgType() == Constant.CHAT_FRI && ChattingTemper.getUserNo() == messageVo.getFromNo())){
            messageVo.setRead(true);
        }else{
            messageVo.setRead(false);
        }
        ChattingTemper.addChattingVo(messageVo, true);
        //插入完成会返回对应的Id
        messageVo.setId(DBManager.getInstance().saveMessage(messageVo));

        return messageVo;
    }
}
