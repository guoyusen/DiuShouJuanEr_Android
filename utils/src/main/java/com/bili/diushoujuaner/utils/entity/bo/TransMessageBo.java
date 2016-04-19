package com.bili.diushoujuaner.utils.entity.bo;

import com.bili.diushoujuaner.utils.Common;
import com.bili.diushoujuaner.utils.Constant;
import com.bili.diushoujuaner.utils.entity.vo.MessageVo;

/**
 * Created by BiLi on 2016/4/17.
 */
public class TransMessageBo {

    private MessageVo messageVo;
    private String lastTime;
    private int sendCount;
    private int status;

    public TransMessageBo(MessageVo messageVo) {
        this.lastTime = Common.getCurrentTimeFull();
        this.messageVo = messageVo;
        this.sendCount = 1;
        this.status = Constant.MESSAGE_STATUS_SENDING;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getLastTime() {
        return lastTime;
    }

    public void reSetLastTime() {
        this.lastTime = Common.getCurrentTimeFull();
    }

    public MessageVo getMessageVo() {
        return messageVo;
    }

    public void setMessageVo(MessageVo messageVo) {
        this.messageVo = messageVo;
    }

    public int getSendCount() {
        return sendCount;
    }

    public void setSendCount(int sendCount) {
        this.sendCount = sendCount;
    }
}
