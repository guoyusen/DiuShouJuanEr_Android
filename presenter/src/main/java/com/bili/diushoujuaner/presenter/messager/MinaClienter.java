package com.bili.diushoujuaner.presenter.messager;

import android.os.Handler;
import android.util.Log;

import com.bili.diushoujuaner.model.filterhelper.HeartBeatFilter;
import com.bili.diushoujuaner.utils.ConstantUtil;
import com.bili.diushoujuaner.utils.GsonUtil;
import com.bili.diushoujuaner.utils.entity.dto.MessageDto;

import org.apache.mina.core.future.CloseFuture;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.IoFuture;
import org.apache.mina.core.future.IoFutureListener;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
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
    private Handler handler;

    public MinaClienter(){
        connector = new NioSocketConnector();
        connector.setConnectTimeoutMillis(ConstantUtil.CONNECTTIMEOUT);
        connector.getSessionConfig().setKeepAlive(true);

        protocolCodecFilter = new ProtocolCodecFilter(new ObjectSerializationCodecFactory());
        connector.getFilterChain().addLast("ProtocolCodecFilter", protocolCodecFilter);

        heartBeatFilter = new HeartBeatFilter();
        heartBeatFilter.setForwardEvent(true);// 是否跳到下一个filter
        heartBeatFilter.setRequestInterval(ConstantUtil.IDEL_TIMEOUT_FOR_INTERVAL);
        connector.getFilterChain().addLast("HeartBeatFilter", heartBeatFilter);
        //设定消息处理器
        connector.setHandler(new MinaClientHanler());

        handler = new Handler();
        minaClienter = this;
    }

    public static MinaClienter getInstance(){
        if(minaClienter == null){
            minaClienter = new MinaClienter();
        }
        return minaClienter;
    }

    public boolean isActive(){
        if(session == null){
            return false;
        }
        return session.isActive() && session.isConnected();
    }

    @Override
    public void run() {
        try{
            ConnectFuture future = connector.connect(new InetSocketAddress(ConstantUtil.SERVER_IP, 1314));
            future.awaitUninterruptibly();
            session = future.getSession();
            if(session != null){
                session.getCloseFuture().awaitUninterruptibly();
            }
        }catch(Exception e){
            e.printStackTrace();
            Log.d("guoyusenm","连接服务器失败" + e.getMessage());
        }
        connector.dispose();
    }

    public void reConnect(){
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                connect();
            }
        },5000);
    }

    public void connect(){
        if(!isActive()){
            new MinaClienter().start();
        }
    }

    public void sendMessage(MessageDto messageDto){
        if(session == null){
            return;
        }
        session.write(GsonUtil.getInstance().toJson(messageDto));
    }

    public void disConnect(){
        if(session == null){
            return;
        }
        CloseFuture closeFuture = session.getCloseFuture();
        closeFuture.addListener(new IoFutureListener<IoFuture>() {
            public void operationComplete(IoFuture future) {
                if (future instanceof CloseFuture) {
                    ((CloseFuture) future).setClosed();
                }
            }
        });
        session.closeNow();
    }

}
