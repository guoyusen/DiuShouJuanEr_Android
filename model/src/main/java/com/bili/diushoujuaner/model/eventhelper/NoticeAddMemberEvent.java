package com.bili.diushoujuaner.model.eventhelper;

import com.bili.diushoujuaner.utils.entity.vo.MessageVo;

/**
 * Created by BiLi on 2016/4/26.
 */
public class NoticeAddMemberEvent {

    private MessageVo messageVo;

    public NoticeAddMemberEvent(MessageVo messageVo) {
        this.messageVo = messageVo;
    }

    public MessageVo getMessageVo() {
        return messageVo;
    }

    public void setMessageVo(MessageVo messageVo) {
        this.messageVo = messageVo;
    }
}
