package com.bili.diushoujuaner.presenter.messager;

import com.bili.diushoujuaner.model.tempHelper.ChattingTemper;
import com.bili.diushoujuaner.utils.ConstantUtil;
import com.bili.diushoujuaner.utils.entity.vo.MessageVo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BiLi on 2016/4/26.
 */
public class MessageWatcher {

    private static MessageWatcher messageWatcher;
    private List<OnUpdateMessageListener> messageListenerList;

    public MessageWatcher(){
        messageListenerList = new ArrayList<>();
    }

    public static synchronized MessageWatcher getInstance(){
        if(messageWatcher == null){
            messageWatcher = new MessageWatcher();
        }
        return messageWatcher;
    }

    public void register(OnUpdateMessageListener messageListener){
        messageListenerList.add(messageListener);
    }

    public void unRegister(OnUpdateMessageListener messageListener){
        messageListenerList.remove(messageListener);
    }

    /**
     * 将消息通知给监听者
     * @param messageVo
     */
    public void publishToListener(MessageVo messageVo){
        //通知所有的监听者
        //正在聊天界面
        //聊天类型相同
        //群聊或者单聊
        if(ChattingTemper.getInstance().isChatting() && ChattingTemper.getInstance().getMsgType() == messageVo.getMsgType() &&
                ((ChattingTemper.getInstance().getMsgType() == ConstantUtil.CHAT_PAR && ChattingTemper.getInstance().getToNo() == messageVo.getToNo()) ||
                        ChattingTemper.getInstance().getMsgType() == ConstantUtil.CHAT_FRI && ChattingTemper.getInstance().getToNo() == messageVo.getFromNo())){
            for(OnUpdateMessageListener messageListener : messageListenerList){
                messageListener.onUpdateMessage(messageVo);
            }
        }
    }

}
