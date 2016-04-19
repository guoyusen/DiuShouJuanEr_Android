package com.bili.diushoujuaner.model.filterhelper;

import com.bili.diushoujuaner.model.preferhelper.CustomSessionPreference;
import com.bili.diushoujuaner.utils.Common;
import com.bili.diushoujuaner.utils.Constant;

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
        return Common.isMessageForHeartBeat(o.toString());
    }

    @Override
    public boolean isResponse(IoSession ioSession, Object o) {
        return Common.isMessageForHeartBeat(o.toString());
    }

    @Override
    public Object getResponse(IoSession ioSession, Object o) {
        return Common.getEmptyMessage("",CustomSessionPreference.getInstance().getCustomSession().getUserNo(), Constant.CHAT_PONG);
    }
}
