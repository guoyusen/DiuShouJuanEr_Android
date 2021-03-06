package com.bili.diushoujuaner.model.actionhelper.action;

import android.content.Context;

import com.bili.diushoujuaner.model.actionhelper.IChattingAction;
import com.bili.diushoujuaner.model.actionhelper.respon.ActionResponse;
import com.bili.diushoujuaner.model.apihelper.ApiResponse;
import com.bili.diushoujuaner.model.apihelper.api.ApiAction;
import com.bili.diushoujuaner.model.apihelper.callback.ApiStringCallbackListener;
import com.bili.diushoujuaner.model.callback.ActionStringCallbackListener;
import com.bili.diushoujuaner.model.databasehelper.DBManager;
import com.bili.diushoujuaner.model.eventhelper.RequestContactEvent;
import com.bili.diushoujuaner.model.eventhelper.DeleteContactEvent;
import com.bili.diushoujuaner.model.eventhelper.UnGroupPartyEvent;
import com.bili.diushoujuaner.model.eventhelper.UpdateContactEvent;
import com.bili.diushoujuaner.model.eventhelper.UpdatePartyEvent;
import com.bili.diushoujuaner.model.eventhelper.UpdateReadCountEvent;
import com.bili.diushoujuaner.model.preferhelper.CustomSessionPreference;
import com.bili.diushoujuaner.model.tempHelper.ChattingTemper;
import com.bili.diushoujuaner.model.tempHelper.ContactTemper;
import com.bili.diushoujuaner.utils.ConstantUtil;
import com.bili.diushoujuaner.utils.EntityUtil;
import com.bili.diushoujuaner.utils.GsonUtil;
import com.bili.diushoujuaner.utils.TimeUtil;
import com.bili.diushoujuaner.utils.entity.dto.ContactDto;
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
    public void getChattingList(final ActionStringCallbackListener<ActionResponse<Void>> actionStringCallbackListener) {
        Tasks.executeInBackground(context, new BackgroundWork<ActionResponse<Void>>() {
            @Override
            public ActionResponse<Void> doInBackground() throws Exception {
                DBManager.getInstance().getRecentMessage();
                return ActionResponse.getActionRespon(null);
            }
        }, new Completion<ActionResponse<Void>>() {
            @Override
            public void onSuccess(Context context, ActionResponse<Void> result) {
                EventBus.getDefault().post(new UpdateReadCountEvent(ConstantUtil.UNREAD_COUNT_MESSAGE, ChattingTemper.getUnReadCount()));
                actionStringCallbackListener.onSuccess(result);
            }

            @Override
            public void onError(Context context, Exception e) {
                actionStringCallbackListener.onSuccess(ActionResponse.<Void>getActionResponError());
            }
        });
    }

    @Override
    public void getOffMessage(final ActionStringCallbackListener<ActionResponse<List<MessageVo>>> actionStringCallbackListener) {
        ApiAction.getInstance().getOffMsg(new ApiStringCallbackListener() {
            @Override
            public void onSuccess(final String data) {
                Tasks.executeInBackground(context, new BackgroundWork<ActionResponse<List<MessageVo>>>() {
                    @Override
                    public ActionResponse<List<MessageVo>> doInBackground() throws Exception {
                        ApiResponse<List<OffMsgDto>> result = GsonUtil.getInstance().fromJson(data, new TypeToken<ApiResponse<List<OffMsgDto>>>(){}.getType());
                        if(result.isLegal()){
                            return processOffMessage(result.getData());
                        }else{
                            return ActionResponse.getActionRespon(result.getMessage(), result.getRetCode(),null);
                        }

                    }
                }, new Completion<ActionResponse<List<MessageVo>>>() {
                    @Override
                    public void onSuccess(Context context, ActionResponse<List<MessageVo>> result) {
                        EventBus.getDefault().post(new UpdateReadCountEvent(ConstantUtil.UNREAD_COUNT_MESSAGE, ChattingTemper.getUnReadCount()));
                        actionStringCallbackListener.onSuccess(result);
                    }

                    @Override
                    public void onError(Context context, Exception e) {
                        actionStringCallbackListener.onSuccess(ActionResponse.<List<MessageVo>>getActionResponError());
                    }
                });
            }

            @Override
            public void onFailure(int errorCode) {
                actionStringCallbackListener.onFailure(errorCode);
            }
        });
    }

    private ActionResponse<List<MessageVo>> processOffMessage(List<OffMsgDto> messageList){
        List<MessageVo> msgDtoList = new ArrayList<>();
        for(OffMsgDto item : messageList){
            switch(item.getMsgType()){
                case ConstantUtil.CHAT_FRI:
                    msgDtoList.add(processOffMessage(item));
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
                case ConstantUtil.CHAT_FRIEND_APPLY:
                    DBManager.getInstance().saveApply(item.getFromNo(), item.getToNo(),item.getContent(), item.getTime(), ConstantUtil.CHAT_FRIEND_APPLY);
                    ContactAction.getInstance(context).getAddContact(item.getFromNo(), ConstantUtil.CONTACT_FRIEND_APPLY_INFO_BEFORE, true);
                    break;
                case ConstantUtil.CHAT_FRIEND_RECOMMEND:
                    DBManager.getInstance().saveApply(item.getFromNo(), item.getToNo(),item.getContent(), item.getTime(), ConstantUtil.CHAT_FRIEND_RECOMMEND);
                    ContactAction.getInstance(context).getAddContact(item.getFromNo(), ConstantUtil.CONTACT_FRIEND_APPLY_INFO_BEFORE, true);
                    break;
                case ConstantUtil.CHAT_PARTY_APPLY:
                    DBManager.getInstance().saveApply(item.getFromNo(), Long.valueOf(item.getTime()),item.getContent(), TimeUtil.getCurrentTimeYYMMDD_HHMMSS(), ConstantUtil.CHAT_PARTY_APPLY);
                    EventBus.getDefault().post(new RequestContactEvent());
                    break;
                case ConstantUtil.CHAT_FRIEND_DELETE:
                    DBManager.getInstance().deleteFriend(item.getFromNo());
                    ChattingTemper.getInstance().deleteChattingVo(item.getFromNo(), ConstantUtil.CHAT_FRI);
                    EventBus.getDefault().post(new DeleteContactEvent(ConstantUtil.DELETE_CONTACT_FRIEND, item.getFromNo()));
                    break;
                case ConstantUtil.CHAT_FRIEND_APPLY_AGREE:
                    if(DBManager.getInstance().isFriended(item.getFromNo())){
                        //收到添加同意信息，获取或者更新本地联系人信息
                        ContactAction.getInstance(context).getAddContact(item.getFromNo(), ConstantUtil.CONTACT_FRIEND_APPLY_INFO_AFTER, true);
                    }
                    item.setMsgType(ConstantUtil.CHAT_FRI);
                    msgDtoList.add(processOffMessage(item));
                    break;
                case ConstantUtil.CHAT_PARTY_APPLY_AGREE:
                    if(item.getFromNo() == CustomSessionPreference.getInstance().getCustomSession().getUserNo()){
                        // 是自己，全量获取该群的所有信息，存入本地，通知更新界面
                        ContactAction.getInstance(context).getWholePartyInfo(item.getToNo(), item.getFromNo(), item.getTime(), true);
                    }else{
                        // 不是自己，那么已经是该群的成员，只需要添加单个人的信息到数据库，通知更新界面
                        ContactAction.getInstance(context).getSingleMemberInfo(item.getToNo(), item.getFromNo(), item.getTime(), true);
                    }
                    break;
                case ConstantUtil.CHAT_PARTY_MEMBER_EXIT:
                    DBManager.getInstance().deleteMember(item.getToNo(), item.getFromNo());
                    DBManager.getInstance().deletePartyChat(item.getToNo(), item.getFromNo());
                    EventBus.getDefault().post(new UpdateContactEvent());
                    EventBus.getDefault().post(new UpdatePartyEvent(item.getToNo(),item.getFromNo(), "", ConstantUtil.CHAT_PARTY_MEMBER_EXIT));
                    break;
                case ConstantUtil.CHAT_MEMBER_BATCH_ADD:
                    ContactDto contactDto = GsonUtil.getInstance().fromJson(item.getContent(), new TypeToken<ContactDto>(){}.getType());
                    ContactAction.getInstance(context).saveNewMembersInfo(contactDto, item.getToNo(), item.getFromNo(), true);
                    break;
                case ConstantUtil.CHAT_PARTY_UNGROUP:
                    DBManager.getInstance().deleteParty(Long.valueOf(item.getContent()));
                    ChattingTemper.getInstance().deleteChattingVo(Long.valueOf(item.getContent()), ConstantUtil.CHAT_PAR);
                    EventBus.getDefault().post(new UpdateContactEvent());
                    EventBus.getDefault().post(new UnGroupPartyEvent(Long.valueOf(item.getContent())));
                    break;
            }
        }
        return ActionResponse.getActionRespon(msgDtoList);
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
