package com.bili.diushoujuaner.presenter.presenter.impl;

import android.content.Context;

import com.bili.diushoujuaner.model.actionhelper.action.MessageAction;
import com.bili.diushoujuaner.model.actionhelper.respon.ActionRespon;
import com.bili.diushoujuaner.model.callback.ActionStringCallbackListener;
import com.bili.diushoujuaner.model.eventhelper.UpdateMessageEvent;
import com.bili.diushoujuaner.model.preferhelper.CustomSessionPreference;
import com.bili.diushoujuaner.model.tempHelper.ChattingTemper;
import com.bili.diushoujuaner.model.tempHelper.ContactTemper;
import com.bili.diushoujuaner.presenter.base.BasePresenter;
import com.bili.diushoujuaner.presenter.messager.LocalClient;
import com.bili.diushoujuaner.presenter.presenter.MessageActivityPresenter;
import com.bili.diushoujuaner.presenter.view.IMessageView;
import com.bili.diushoujuaner.utils.Common;
import com.bili.diushoujuaner.utils.Constant;
import com.bili.diushoujuaner.utils.entity.vo.MessageVo;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by BiLi on 2016/4/14.
 */
public class MessageActivityPresenterImpl extends BasePresenter<IMessageView> implements MessageActivityPresenter {

    private MessageSearchParam messageSearchParam;

    class MessageSearchParam{
        private long lastId;
        private int pageIndex;
        private int pageSize;
        private boolean loadComplete;

        public MessageSearchParam(long lastId, int pageIndex, int pageSize, boolean loadComplete) {
            this.lastId = lastId;
            this.pageSize = pageSize;
            this.pageIndex = pageIndex;
            this.loadComplete = loadComplete;
        }

        public boolean isLoadComplete() {
            return loadComplete;
        }

        public void setLoadComplete(boolean loadComplete) {
            this.loadComplete = loadComplete;
        }

        public boolean isLoadMore(){
            return pageIndex != 1;
        }

        public long getLastId() {
            return lastId;
        }

        public void setLastId(long lastId) {
            this.lastId = lastId;
        }

        public int getPageIndex() {
            return pageIndex;
        }

        public void setPageIndex(int pageIndex) {
            this.pageIndex = pageIndex;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }
    }

    public MessageActivityPresenterImpl(IMessageView baseView, Context context) {
        super(baseView, context);
        messageSearchParam = new MessageSearchParam(-1, 1, 40, false);
    }

    @Override
    public void getNextActivity() {
        if(ChattingTemper.getInstance().getMsgType() == Constant.CHAT_FRI){
            if(isBindViewValid()){
                getBindView().showNextActivity(Constant.SHOW_TYPE_CHATTING_SETTING);
            }
        }else{
            if(isBindViewValid()){
                getBindView().showNextActivity(Constant.SHOW_TYPE_PARTY_DETAIL);
            }
        }
    }

    @Override
    public void clearCurrentChat() {
        ChattingTemper.getInstance().resetCurrentChatBo();
    }

    @Override
    public boolean validateUpdateEvent(UpdateMessageEvent updateMessageEvent) {
        switch ( updateMessageEvent.getType()){
            case UpdateMessageEvent.MESSAGE_SEND:
            case UpdateMessageEvent.MESSAGE_STATUS:
                return updateMessageEvent.getMessageVo().getToNo() == ChattingTemper.getInstance().getToNo();
            case UpdateMessageEvent.MESSAGE_RECEIVE:
                return (updateMessageEvent.getMessageVo().getMsgType() == Constant.CHAT_FRI && updateMessageEvent.getMessageVo().getFromNo() == ChattingTemper.getInstance().getToNo())
                        || (updateMessageEvent.getMessageVo().getMsgType() == Constant.CHAT_PAR && updateMessageEvent.getMessageVo().getToNo() == ChattingTemper.getInstance().getToNo());
        }
        return false;
    }

    @Override
    public void saveMessageVo(String content, int conType) {
        final MessageVo messageVo = getSendingMessageVo(content, conType);
        MessageAction.getInstance(context).saveMessageVo(messageVo, new ActionStringCallbackListener<ActionRespon<MessageVo>>() {
            @Override
            public void onSuccess(ActionRespon<MessageVo> result) {
                if(showMessage(result.getRetCode(), result.getMessage())){
                    ChattingTemper.getInstance().addChattingVoFromLocal(result.getData());
                    EventBus.getDefault().post(new UpdateMessageEvent(result.getData(), UpdateMessageEvent.MESSAGE_SEND));
                    //发送到service
                    LocalClient.getInstance(context).sendMessageToService(Constant.HANDLER_CHAT, result.getData());
                }
            }

            @Override
            public void onFailure(int errorCode) {
                showError(errorCode);
            }
        });
    }

    private MessageVo getSendingMessageVo(String content, int conType){
        MessageVo messageVo = new MessageVo();
        messageVo.setMsgType(ChattingTemper.getInstance().getMsgType());
        messageVo.setTime(Common.getCurrentTimeYYMMDD_HHMMSS());
        messageVo.setContent(content);
        messageVo.setConType(conType);
        messageVo.setFromNo(CustomSessionPreference.getInstance().getCustomSession().getUserNo());
        messageVo.setToNo(ChattingTemper.getInstance().getToNo());
        messageVo.setRead(true);
        messageVo.setStatus(Constant.MESSAGE_STATUS_SENDING);
        messageVo.setTimeShow(ChattingTemper.getInstance().getIsNeedShowTime(messageVo));
        messageVo.setSerialNo(Common.getSerialNo());

        return messageVo;
    }

    @Override
    public long getOwnerNo() {
        return CustomSessionPreference.getInstance().getCustomSession().getUserNo();
    }

    @Override
    public void resetMessageSearchParam(long rowId, int pageIndex) {
        messageSearchParam.setLastId(rowId);
        messageSearchParam.setPageIndex(pageIndex);
        messageSearchParam.setLoadComplete(false);
    }

    @Override
    public void getContactInfo() {
        if(ChattingTemper.getInstance().getMsgType() == Constant.CHAT_PAR){
            getBindView().showContactInfo(ContactTemper.getInstance().getPartyVo(ChattingTemper.getInstance().getToNo()));
        }else if(ChattingTemper.getInstance().getMsgType() == Constant.CHAT_FRI){
            getBindView().showContactInfo(ContactTemper.getInstance().getFriendVo(ChattingTemper.getInstance().getToNo()));
        }

    }

    @Override
    public void getMessageList() {
        if(messageSearchParam.isLoadComplete()){
            getBindView().loadComplete();
            return;
        }
        MessageAction.getInstance(context).getMessageList(messageSearchParam.getLastId(), messageSearchParam.getPageIndex(), messageSearchParam.getPageSize(), new ActionStringCallbackListener<ActionRespon<List<MessageVo>>>() {
            @Override
            public void onSuccess(ActionRespon<List<MessageVo>> result) {
                if(showMessage(result.getRetCode(), result.getMessage())){
                    if(isBindViewValid()){
                        if(messageSearchParam.isLoadMore()){
                            getBindView().loadFinish();
                            getBindView().showMoreMessageList(result.getData());
                        }else{
                            if(!result.getData().isEmpty()){
                                messageSearchParam.setLastId(result.getData().get(result.getData().size() - 1).getId());
                            }
                            getBindView().showMessageList(result.getData());
                        }
                    }
                    if(result.getData().size() == messageSearchParam.getPageSize()){
                        messageSearchParam.setPageIndex(messageSearchParam.getPageIndex() + 1);
                        messageSearchParam.setLoadComplete(false);
                    }else if(result.getData().size() < messageSearchParam.getPageSize()){
                        messageSearchParam.setLoadComplete(true);
                    }
                }
            }

            @Override
            public void onFailure(int errorCode) {
                showError(errorCode);
            }
        });
    }
}
