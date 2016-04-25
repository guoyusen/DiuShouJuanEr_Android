package com.bili.diushoujuaner.model.filterhelper;

import android.util.Log;

import com.bili.diushoujuaner.utils.ConstantUtil;
import com.bili.diushoujuaner.utils.EntityUtil;
import com.bili.diushoujuaner.utils.entity.dto.MessageDto;

import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.write.WriteRequest;
import org.apache.mina.filter.keepalive.KeepAliveFilter;

/**
 * Created by BiLi on 2016/4/15.
 */
public class HeartBeatFilter extends KeepAliveFilter {

    public HeartBeatFilter() {
        super(new KeepAliveMessageFactoryImpl(), IdleStatus.BOTH_IDLE);
    }

    @Override
    public void messageSent(NextFilter nextFilter, IoSession session, WriteRequest writeRequest) throws Exception {
        Log.d("guoyusenm",Thread.currentThread().getName());
        if(getPermitSent(writeRequest.getMessage().toString())){
            Log.d("guoyusenm","是是是是上层需要的消息");
            super.messageSent(nextFilter, session, writeRequest);
        }else{
            Log.d("guoyusenm","不不不是上层需要的消息");
        }
    }


    /**
     * 判断是否是上层需要的消息
     * @param message
     * @return
     */
    private boolean getPermitSent(String message) {
        if (message.substring(0, 1).equals("{") && message.substring(message.length() - 1,message.length()).equals("}")) {
            MessageDto messageDto = EntityUtil.getMessageDtoFromJSONString(message);
            return !(messageDto == null || messageDto.getMsgType() == ConstantUtil.CHAT_PING || messageDto.getMsgType() == ConstantUtil.CHAT_PONG);
        } else {
            return false;
        }
    }
}
