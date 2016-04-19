package com.bili.diushoujuaner.model.eventhelper;

import com.bili.diushoujuaner.utils.entity.vo.MessageVo;

/**
 * Created by BiLi on 2016/4/16.
 */
public class UpdateMessageEvent {

    public static final int MESSAGE_RECEIVE = 0;
    public static final int MESSAGE_SEND = 1;
    public static final int MESSAGE_STATUS = 2;

    private MessageVo messageVo;
    private int type;

    public UpdateMessageEvent(MessageVo messageVo, int type) {
        this.messageVo = messageVo;
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public MessageVo getMessageVo() {
        return messageVo;
    }

    public void setMessageVo(MessageVo messageVo) {
        this.messageVo = messageVo;
    }
}
