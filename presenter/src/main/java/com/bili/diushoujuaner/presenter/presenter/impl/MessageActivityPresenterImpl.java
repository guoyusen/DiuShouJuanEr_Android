package com.bili.diushoujuaner.presenter.presenter.impl;

import android.content.Context;

import com.bili.diushoujuaner.model.actionhelper.action.ContactAction;
import com.bili.diushoujuaner.model.actionhelper.action.MessageAction;
import com.bili.diushoujuaner.model.actionhelper.respon.ActionRespon;
import com.bili.diushoujuaner.model.apihelper.request.ContactInfoReq;
import com.bili.diushoujuaner.model.callback.ActionStringCallbackListener;
import com.bili.diushoujuaner.model.preferhelper.CustomSessionPreference;
import com.bili.diushoujuaner.model.tempHelper.ChattingTemper;
import com.bili.diushoujuaner.model.tempHelper.ContactTemper;
import com.bili.diushoujuaner.presenter.base.BasePresenter;
import com.bili.diushoujuaner.presenter.presenter.MessageActivityPresenter;
import com.bili.diushoujuaner.presenter.view.IMessageView;
import com.bili.diushoujuaner.utils.Common;
import com.bili.diushoujuaner.utils.Constant;
import com.bili.diushoujuaner.utils.entity.vo.FriendVo;
import com.bili.diushoujuaner.utils.entity.vo.MessageVo;

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
        private boolean loadOver;

        public MessageSearchParam(long lastId, int pageIndex, int pageSize, boolean loadOver) {
            this.lastId = lastId;
            this.pageSize = pageSize;
            this.pageIndex = pageIndex;
            this.loadOver = loadOver;
        }

        public boolean isLoadOver() {
            return loadOver;
        }

        public void setLoadOver(boolean loadOver) {
            this.loadOver = loadOver;
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
    public long getOwnerNo() {
        return CustomSessionPreference.getInstance().getCustomSession().getUserNo();
    }

    @Override
    public void resetMessageSearchParam(long rowId, int pageIndex) {
        messageSearchParam.setLastId(rowId);
        messageSearchParam.setPageIndex(pageIndex);
        messageSearchParam.setLoadOver(false);
    }

    @Override
    public void getContactInfo() {
        if(ChattingTemper.getMsgType() == Constant.CHAT_PAR){
            getBindView().showContactInfo(ContactTemper.getPartyVo(ChattingTemper.getUserNo()));
        }else if(ChattingTemper.getMsgType() == Constant.CHAT_FRI){
            getBindView().showContactInfo(ContactTemper.getFriendVo(ChattingTemper.getUserNo()));
        }

    }

    @Override
    public void getMessageList() {
        if(messageSearchParam.isLoadOver()){
            return;
        }
        MessageAction.getInstance(context).getMessageList(messageSearchParam.getLastId(), messageSearchParam.getPageIndex(), messageSearchParam.getPageSize(), new ActionStringCallbackListener<ActionRespon<List<MessageVo>>>() {
            @Override
            public void onSuccess(ActionRespon<List<MessageVo>> result) {
                if(showMessage(result.getRetCode(), result.getMessage())){
                    if(result.getData().size() == messageSearchParam.getPageSize()){
                        messageSearchParam.setLoadOver(false);
                    }else if(result.getData().size() < messageSearchParam.getPageSize()){
                        messageSearchParam.setLoadOver(true);
                    }
                    if(isBindViewValid()){
                        getBindView().showMessageList(result.getData());
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
