package com.bili.diushoujuaner.presenter.presenter.impl;

import android.content.Context;

import com.bili.diushoujuaner.model.actionhelper.action.ChattingAction;
import com.bili.diushoujuaner.model.actionhelper.respon.ActionRespon;
import com.bili.diushoujuaner.model.callback.ActionStringCallbackListener;
import com.bili.diushoujuaner.model.eventhelper.UpdateReadCountEvent;
import com.bili.diushoujuaner.model.tempHelper.ChattingTemper;
import com.bili.diushoujuaner.presenter.base.BasePresenter;
import com.bili.diushoujuaner.presenter.presenter.ChattingFragmentPresenter;
import com.bili.diushoujuaner.presenter.view.IChattingView;
import com.bili.diushoujuaner.utils.ConstantUtil;
import com.bili.diushoujuaner.utils.entity.vo.MessageVo;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by BiLi on 2016/4/12.
 */
public class ChattingFragmentPresenterImpl extends BasePresenter<IChattingView> implements ChattingFragmentPresenter {

    public ChattingFragmentPresenterImpl(IChattingView baseView, Context context) {
        super(baseView, context);
    }

    @Override
    public void deleteChattingVo(long userNo, int msgType) {
        ChattingTemper.getInstance().deleteChattingVo(userNo, msgType);
        EventBus.getDefault().post(new UpdateReadCountEvent(ConstantUtil.UNREAD_COUNT_MESSAGE, ChattingTemper.getUnReadCount()));
        ChattingAction.getInstance(context).deleteRecent(userNo, msgType);
    }

    @Override
    public void updateMessageRead(long userNo,  int msgType) {
        ChattingTemper.getInstance().updateChattingVoRead(userNo, msgType);
        ChattingAction.getInstance(context).updateMessageRead(userNo, msgType);
        EventBus.getDefault().post(new UpdateReadCountEvent(ConstantUtil.UNREAD_COUNT_MESSAGE, ChattingTemper.getUnReadCount()));
    }

    @Override
    public void getChattingVoListFromTemper() {
        if(isBindViewValid()){
            getBindView().showChatting(ChattingTemper.getInstance().getChattingVoList());
            EventBus.getDefault().post(new UpdateReadCountEvent(ConstantUtil.UNREAD_COUNT_MESSAGE, ChattingTemper.getUnReadCount()));
        }
    }

    @Override
    public void setCurrentChatting(long userNo, int msgType) {
        ChattingTemper.getInstance().setCurrentChatBo(userNo, msgType);
    }

    @Override
    public void getChattingList() {
        ChattingAction.getInstance(context).getChattingList(new ActionStringCallbackListener<ActionRespon<Void>>() {
            @Override
            public void onSuccess(ActionRespon<Void> result) {
                if(showMessage(result.getRetCode(), result.getMessage())){
                    if(isBindViewValid()){
                        getBindView().showChatting(ChattingTemper.getInstance().getChattingVoList());
                    }
                    EventBus.getDefault().post(new UpdateReadCountEvent(ConstantUtil.UNREAD_COUNT_MESSAGE, ChattingTemper.getUnReadCount()));
                }
            }

            @Override
            public void onFailure(int errorCode) {
                showError(errorCode);
            }
        });

    }

    @Override
    public void getOffMessage() {
        showLoading(ConstantUtil.LOADING_DEFAULT,"");
        ChattingAction.getInstance(context).getOffMessage(new ActionStringCallbackListener<ActionRespon<List<MessageVo>>>() {
            @Override
            public void onSuccess(ActionRespon<List<MessageVo>> result) {
                hideLoading(ConstantUtil.LOADING_DEFAULT);
                if(showMessage(result.getRetCode(), result.getMessage())){
                    for(MessageVo messageVo : result.getData()){
                        ChattingTemper.getInstance().publishToListener(messageVo);
                    }
                    EventBus.getDefault().post(new UpdateReadCountEvent(ConstantUtil.UNREAD_COUNT_MESSAGE, ChattingTemper.getUnReadCount()));
                    if(isBindViewValid()){
                        getBindView().showChatting(ChattingTemper.getInstance().getChattingVoList());
                    }
                }
            }

            @Override
            public void onFailure(int errorCode) {
                hideLoading(ConstantUtil.LOADING_DEFAULT);
                showError(errorCode);
            }
        });
    }
}
