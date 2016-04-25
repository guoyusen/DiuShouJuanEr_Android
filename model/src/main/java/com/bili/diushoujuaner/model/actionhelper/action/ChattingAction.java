package com.bili.diushoujuaner.model.actionhelper.action;

import android.content.Context;

import com.bili.diushoujuaner.model.actionhelper.IChattingAction;
import com.bili.diushoujuaner.model.actionhelper.respon.ActionRespon;
import com.bili.diushoujuaner.model.apihelper.ApiRespon;
import com.bili.diushoujuaner.model.apihelper.api.ApiAction;
import com.bili.diushoujuaner.model.apihelper.callback.ApiStringCallbackListener;
import com.bili.diushoujuaner.model.callback.ActionStringCallbackListener;
import com.bili.diushoujuaner.model.databasehelper.DBManager;
import com.bili.diushoujuaner.model.eventhelper.RequestContactEvent;
import com.bili.diushoujuaner.model.eventhelper.DeleteContactEvent;
import com.bili.diushoujuaner.model.eventhelper.UpdatePartyEvent;
import com.bili.diushoujuaner.model.tempHelper.ChattingTemper;
import com.bili.diushoujuaner.model.tempHelper.ContactTemper;
import com.bili.diushoujuaner.utils.ConstantUtil;
import com.bili.diushoujuaner.utils.EntityUtil;
import com.bili.diushoujuaner.utils.GsonUtil;
import com.bili.diushoujuaner.utils.TimeUtil;
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
                        ApiRespon<List<OffMsgDto>> result = GsonUtil.getInstance().fromJson(data, new TypeToken<ApiRespon<List<OffMsgDto>>>(){}.getType());
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
                case ConstantUtil.CHAT_FRI:
                    msgDtoList.add(processOffMessage(item));
                    if(item.getConType() == ConstantUtil.CHAT_CONTENT_FRIEND_AGREE && DBManager.getInstance().isFriended(item.getFromNo())){
                        //收到添加同意信息，获取或者更新本地联系人信息
                        ContactAction.getInstance(context).getAddContact(item.getFromNo(), ConstantUtil.CONTACT_INFO_ADD_AFTER);
                    }
                    break;
                case ConstantUtil.CHAT_PAR:
                    msgDtoList.add(processOffMessage(item));
                    break;
                case ConstantUtil.CHAT_GOOD:
                    //发送Event通知
                    break;
                case ConstantUtil.CHAT_TIME:
                    break;
                case ConstantUtil.CHAT_PARTY_HEAD:
                    ContactTemper.getInstance().updatePartyHeadPic(item.getToNo(), item.getContent());
                    DBManager.getInstance().updatePartyHeadPic(item.getToNo(), item.getContent());
                    EventBus.getDefault().post(new UpdatePartyEvent(item.getToNo(), item.getContent(), ConstantUtil.CHAT_PARTY_HEAD));
                    break;
                case ConstantUtil.CHAT_PARTY_NAME:
                    ContactTemper.getInstance().updatePartyName(item.getToNo(), item.getContent());
                    DBManager.getInstance().updatePartyName(item.getToNo(), item.getContent());
                    EventBus.getDefault().post(new UpdatePartyEvent(item.getToNo(), item.getContent(), ConstantUtil.CHAT_PARTY_NAME));
                    break;
                case ConstantUtil.CHAT_PARTY_MEMBER_NAME:
                    ContactTemper.getInstance().updateMemberName(item.getToNo(), item.getFromNo(), item.getContent());
                    DBManager.getInstance().updateMemberName(item.getToNo(), item.getFromNo(), item.getContent());
                    EventBus.getDefault().post(new UpdatePartyEvent(item.getToNo(), item.getContent(), ConstantUtil.CHAT_PARTY_MEMBER_NAME));
                    break;
                case ConstantUtil.CHAT_FRIEND_ADD:
                    DBManager.getInstance().saveApply(item.getFromNo(), item.getToNo(),item.getContent(), item.getTime(), ConstantUtil.CHAT_FRIEND_ADD);
                    ContactAction.getInstance(context).getAddContact(item.getFromNo(), ConstantUtil.CONTACT_INFO_ADD_BEFORE);
                    break;
                case ConstantUtil.CHAT_PARTY_ADD:
                    DBManager.getInstance().saveApply(item.getFromNo(), Long.valueOf(item.getTime()),item.getContent(), TimeUtil.getCurrentTimeYYMMDD_HHMMSS(), ConstantUtil.CHAT_PARTY_ADD);
                    EventBus.getDefault().post(new RequestContactEvent());
                    break;
                case ConstantUtil.CHAT_FRIEND_DELETE:
                    DBManager.getInstance().deleteFriend(item.getFromNo());
                    ChattingTemper.getInstance().deleteChattingVo(item.getFromNo(), ConstantUtil.CHAT_FRI);
                    EventBus.getDefault().post(new DeleteContactEvent(ConstantUtil.DELETE_CONTACT_FRIEND, item.getFromNo()));
                    break;
            }
        }
        return ActionRespon.getActionRespon(msgDtoList);
    }

    private MessageVo processOffMessage(OffMsgDto item){
        MessageVo messageVo = EntityUtil.getMessageVoFromOffMsgDto(item);
        //接收到的离线信息，需要插在首位，且此时需要判断是否显示时间
        ChattingTemper.getInstance().addChattingVoFromServer(messageVo);
        if(messageVo.getMsgType() == ConstantUtil.CHAT_FRI){
            DBManager.getInstance().updateFriendRecent(messageVo.getFromNo(), true);
        }else if(messageVo.getMsgType() == ConstantUtil.CHAT_PAR){
            DBManager.getInstance().updateMemberRecent(messageVo.getToNo(), true);
        }
        //插入完成会返回包含id的messageVo
        return DBManager.getInstance().saveMessage(messageVo);
    }
}
