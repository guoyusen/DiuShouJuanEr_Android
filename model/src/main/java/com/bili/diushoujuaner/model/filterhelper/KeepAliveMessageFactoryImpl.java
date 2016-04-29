package com.bili.diushoujuaner.model.filterhelper;

import com.bili.diushoujuaner.model.messagehelper.MessageServiceHandler;
import com.bili.diushoujuaner.model.preferhelper.ConnectionPreference;
import com.bili.diushoujuaner.model.preferhelper.CustomSessionPreference;
import com.bili.diushoujuaner.utils.CommonUtil;
import com.bili.diushoujuaner.utils.ConstantUtil;
import com.bili.diushoujuaner.utils.EntityUtil;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.keepalive.KeepAliveMessageFactory;

/**
 * Created by BiLi on 2016/4/15.
 */
public class KeepAliveMessageFactoryImpl implements KeepAliveMessageFactory {

    @Override
    public Object getRequest(IoSession ioSession) {
        return null;
    }

    @Override
    public boolean isRequest(IoSession ioSession, Object o) {
        return CommonUtil.isMessageForHeartBeat(o.toString());
    }

    @Override
    public boolean isResponse(IoSession ioSession, Object o) {
        return CommonUtil.isMessageForHeartBeat(o.toString());
    }

    @Override
    public Object getResponse(IoSession ioSession, Object o) {
        ConnectionPreference.getInstance().saveState(true);
        MessageServiceHandler.getInstance().sendMessageToClient(ConstantUtil.HANDLER_HEARTBEAT, null);
        return EntityUtil.getEmptyMessage("",CustomSessionPreference.getInstance().getCustomSession().getUserNo(), ConstantUtil.CHAT_PONG);
    }
}
