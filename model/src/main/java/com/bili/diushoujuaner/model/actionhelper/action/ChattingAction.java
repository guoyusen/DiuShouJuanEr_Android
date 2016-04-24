package com.bili.diushoujuaner.model.actionhelper.action;

import android.content.Context;

import com.bili.diushoujuaner.model.actionhelper.IChattingAction;
import com.bili.diushoujuaner.model.actionhelper.respon.ActionRespon;
import com.bili.diushoujuaner.model.apihelper.ApiRespon;
import com.bili.diushoujuaner.model.apihelper.api.ApiAction;
import com.bili.diushoujuaner.model.apihelper.callback.ApiStringCallbackListener;
import com.bili.diushoujuaner.model.cachehelper.ACache;
import com.bili.diushoujuaner.model.callback.ActionStringCallbackListener;
import com.bili.diushoujuaner.model.databasehelper.DBManager;
import com.bili.diushoujuaner.model.eventhelper.AddContactEvent;
import com.bili.diushoujuaner.model.eventhelper.UpdateContactEvent;
import com.bili.diushoujuaner.model.eventhelper.UpdatePartyEvent;
import com.bili.diushoujuaner.model.tempHelper.ChattingTemper;
import com.bili.diushoujuaner.model.tempHelper.ContactTemper;
import com.bili.diushoujuaner.utils.Common;
import com.bili.diushoujuaner.utils.Constant;
import com.bili.diushoujuaner.utils.GsonParser;
import com.bili.diushoujuaner.utils.entity.dto.OffMsgDto;
import com.bili.diushoujuaner.utils.entity.vo.MessageVo;
import com.google.gson.reflect.TypeToken;
import com.nanotasks.BackgroundWork;
import com.nanotasks.Completion;
import com.nanotasks.Tasks;

import org.greenrobot.eventbus.EventBus;

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
    public void deleteRecent(final long userNo, final int msgType) {
        Tasks.executeInBackground(context, new BackgroundWork<Void>() {
            @Override
            public Void doInBackground() throws Exception {
                DBManager.getInstance().deleteRecent(userNo, msgType);
                return null;
            }
        }, new Completion<Void>() {
            @Override
            public void onSuccess(Context context, Void result) {
            }

            @Override
            public void onError(Context context, Exception e) {
            }
        });
    }

    @Override
    public void updateMessageRead(final long userNo, final int msgType) {
        Tasks.executeInBackground(context, new BackgroundWork<Void>() {
            @Override
            public Void doInBackground() throws Exception {
                DBManager.getInstance().updateMessageRead(userNo, msgType);
                return null;
            }
        }, new Completion<Void>() {
            @Override
            public void onSuccess(Context context, Void result) {
            }

            @Override
            public void onError(Context context, Exception e) {
            }
        });
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
        List<MessageVo> msgDtoList = new ArrayList<>();
        for(OffMsgDto item : messageList){
            switch(item.getMsgType()){
                case Constant.CHAT_FRI:
                    msgDtoList.add(processOffMessage(item));
                    if(item.getConType() == Constant.CHAT_CONTENT_FRIEND_AGREE && DBManager.getInstance().isFriended(item.getFromNo())){
                        //收到添加同意信息，强制刷新联系人列表
                        ACache.getInstance().put(Constant.ACACHE_LAST_TIME_CONTACT, "");
                        EventBus.getDefault().post(new UpdateContactEvent());
                    }
                    break;
                case Constant.CHAT_PAR:
                    msgDtoList.add(processOffMessage(item));
                    break;
                case Constant.CHAT_GOOD:
                    //发送Event通知
                    break;
                case Constant.CHAT_TIME:
                    break;
                case Constant.CHAT_PARTY_HEAD:
                    ContactTemper.getInstance().updatePartyHeadPic(item.getToNo(), item.getContent());
                    DBManager.getInstance().updatePartyHeadPic(item.getToNo(), item.getContent());
                    EventBus.getDefault().post(new UpdatePartyEvent(item.getToNo(), item.getContent(), Constant.CHAT_PARTY_HEAD));
                    break;
                case Constant.CHAT_PARTY_NAME:
                    ContactTemper.getInstance().updatePartyName(item.getToNo(), item.getContent());
                    DBManager.getInstance().updatePartyName(item.getToNo(), item.getContent());
                    EventBus.getDefault().post(new UpdatePartyEvent(item.getToNo(), item.getContent(), Constant.CHAT_PARTY_NAME));
                    break;
                case Constant.CHAT_PARTY_MEMBER_NAME:
                    ContactTemper.getInstance().updateMemberName(item.getToNo(), item.getFromNo(), item.getContent());
                    DBManager.getInstance().updateMemberName(item.getToNo(), item.getFromNo(), item.getContent());
                    EventBus.getDefault().post(new UpdatePartyEvent(item.getToNo(), item.getContent(), Constant.CHAT_PARTY_MEMBER_NAME));
                    break;
                case Constant.CHAT_FRIEND_ADD:
                    DBManager.getInstance().saveApply(item.getFromNo(), item.getToNo(),item.getContent(), item.getTime(), Constant.CHAT_FRIEND_ADD);
                    ContactAction.getInstance(context).getAddContact(item.getFromNo());
                    break;
                case Constant.CHAT_PARTY_ADD:
                    DBManager.getInstance().saveApply(item.getFromNo(), Long.valueOf(item.getTime()),item.getContent(), Common.getCurrentTimeYYMMDD_HHMMSS(), Constant.CHAT_PARTY_ADD);
                    EventBus.getDefault().post(new AddContactEvent());
                    break;
            }
        }
        return ActionRespon.getActionRespon(msgDtoList);
    }

    private MessageVo processOffMessage(OffMsgDto item){
        MessageVo messageVo = Common.getMessageVoFromOffMsgDto(item);
        //接收到的离线信息，需要插在首位，且此时需要判断是否显示时间
        ChattingTemper.getInstance().addChattingVoFromServer(messageVo);
        if(messageVo.getMsgType() == Constant.CHAT_FRI){
            DBManager.getInstance().updateFriendRecent(messageVo.getFromNo(), true);
        }else if(messageVo.getMsgType() == Constant.CHAT_PAR){
            DBManager.getInstance().updateMemberRecent(messageVo.getToNo(), true);
        }
        //插入完成会返回包含id的messageVo
        return DBManager.getInstance().saveMessage(messageVo);
    }
}
