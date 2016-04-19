package com.bili.diushoujuaner.presenter.messager;

import android.util.Log;

import com.bili.diushoujuaner.model.filterhelper.HeartBeatFilter;
import com.bili.diushoujuaner.utils.Constant;
import com.bili.diushoujuaner.utils.GsonParser;
import com.bili.diushoujuaner.utils.entity.dto.MessageDto;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import java.net.InetSocketAddress;

/**
 * Created by BiLi on 2016/4/15.
 */
public class MinaClienter extends Thread{

    private static MinaClienter minaClienter;
    private NioSocketConnector connector;
    private ProtocolCodecFilter protocolCodecFilter;
    private HeartBeatFilter heartBeatFilter;
    private IoSession session;

    public MinaClienter(){
        connector = new NioSocketConnector();
        connector.setConnectTimeoutMillis(Constant.CONNECTTIMEOUT);
        connector.getSessionConfig().setKeepAlive(true);

        protocolCodecFilter = new ProtocolCodecFilter(new ObjectSerializationCodecFactory());
        connector.getFilterChain().addLast("ProtocolCodecFilter", protocolCodecFilter);

        heartBeatFilter = new HeartBeatFilter();
        heartBeatFilter.setForwardEvent(true);// 是否跳到下一个filter
        heartBeatFilter.setRequestInterval(Constant.IDEL_TIMEOUT_FOR_INTERVAL);
        connector.getFilterChain().addLast("HeartBeatFilter", heartBeatFilter);
        //设定消息处理器
        connector.setHandler(new MinaClientHanler());
    }

    public static synchronized MinaClienter getInstance(){
        if(minaClienter == null){
            minaClienter = new MinaClienter();
        }
        return minaClienter;
    }

    @Override
    public void run() {
        try{
            ConnectFuture future = connector.connect(new InetSocketAddress(Constant.SERVER_IP, 1314));
            future.awaitUninterruptibly();
            session = future.getSession();
            if(session != null){
                session.getCloseFuture().awaitUninterruptibly();
            }
        }catch(Exception e){
            e.printStackTrace();
            Log.d("guoyusenm","聊天客户端连接服务器失败" + e.getMessage());
        }
        connector.dispose();
    }

    public void sendMessage(MessageDto messageDto){
        if(session == null){
            return;
        }
        session.write(GsonParser.getInstance().toJson(messageDto));
    }

}
