package com.bili.diushoujuaner.presenter.messager;

import android.util.Log;

import com.bili.diushoujuaner.model.databasehelper.DBManager;
import com.bili.diushoujuaner.model.eventhelper.LoginSuccessEvent;
import com.bili.diushoujuaner.model.eventhelper.UpdateMessageEvent;
import com.bili.diushoujuaner.model.tempHelper.ChattingTemper;
import com.bili.diushoujuaner.utils.Common;
import com.bili.diushoujuaner.utils.Constant;
import com.bili.diushoujuaner.utils.entity.bo.TransMessageBo;
import com.bili.diushoujuaner.utils.entity.vo.MessageVo;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by BiLi on 2016/4/17.
 */
public class Transceiver extends Thread {

    //消息暂存 <SerialNo, TransMessageBo>
    private HashMap<String, TransMessageBo> transMessageBoHashMap;
    final private List<TransMessageBo> transMessageBoList;
    private static final int MAX_SAND_TIMES = 3;//最大重发次数
    private static final int MAX_BETWEEN_TIME = 2000;//发送和接收之间有1000毫秒的间隔，超过1000毫秒没有接收到则认为发送失败
    private static Transceiver transceiver;

    public Transceiver(){
        transMessageBoHashMap = new HashMap<>();
        transMessageBoList = new ArrayList<>();
    }

    public static void init(){
        transceiver = new Transceiver();
        transceiver.start();
    }

    public static synchronized Transceiver getInstance(){
        if(transceiver == null){
            transceiver = new Transceiver();
            transceiver.start();
        }
        return transceiver;
    }

    public void addSendTask(MessageVo messageVo){
        synchronized (transMessageBoList){
            TransMessageBo transMessageBo = new TransMessageBo(messageVo);
            transMessageBoHashMap.put(messageVo.getSerialNo(), transMessageBo);
            transMessageBoList.add(transMessageBo);
            transMessageBoList.notify();
            MinaClienter.getInstance().sendMessage(Common.getMessageDtoFromMessageVo(messageVo));
        }
    }

    public void updateStatusSuccess(String serialNo){
        synchronized (transMessageBoList){
            transMessageBoHashMap.get(serialNo).setStatus(Constant.MESSAGE_STATUS_SUCCESS);
        }
    }

    @Override
    public void run() {
        while(true){
            synchronized (transMessageBoList){
                if(transMessageBoList.isEmpty()){
                    try{
                        Log.d("guoyusenm","收发器清空了");
                        transMessageBoList.wait();
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
                Log.d("guoyusenm","收发器循环中...");
                //循环遍历每个bo，判断状态并执行逻辑
                for(TransMessageBo transMessageBo : transMessageBoList){
                    if(transMessageBo.getStatus() == Constant.MESSAGE_STATUS_SUCCESS){
                        //发送成功，执行通知，更新界面，更新数据库，删除该消息在收发器中的存储
                        transMessageBoHashMap.remove(transMessageBo);
                        transMessageBoList.remove(transMessageBo);
                        if(transMessageBo.getMessageVo().getMsgType() == Constant.CHAT_INIT){
                            EventBus.getDefault().post(new LoginSuccessEvent());
                        }else if(transMessageBo.getMessageVo().getMsgType() == Constant.CHAT_PAR || transMessageBo.getMessageVo().getMsgType() == Constant.CHAT_FRI){
                            Log.d("guoyusenm","删除啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦");
                            transMessageBo.getMessageVo().setStatus(Constant.MESSAGE_STATUS_SUCCESS);
                            ChattingTemper.getInstance().updateChattingVo(transMessageBo.getMessageVo());
                            DBManager.getInstance().updateMessageStatus(transMessageBo.getMessageVo());
                            EventBus.getDefault().post(new UpdateMessageEvent(transMessageBo.getMessageVo(), UpdateMessageEvent.MESSAGE_STATUS));
                            break;
                        }
                    }else if(transMessageBo.getSendCount() >= MAX_SAND_TIMES && Common.getMilliDifferenceBetweenTime(transMessageBo.getLastTime()) > MAX_BETWEEN_TIME && transMessageBo.getStatus() == Constant.MESSAGE_STATUS_SENDING){
                        //发送已经达到3次，相聚上次时间间隔超过1000毫秒，状态还是发送中
                        if(transMessageBo.getMessageVo().getMsgType() == Constant.CHAT_PAR || transMessageBo.getMessageVo().getMsgType() == Constant.CHAT_FRI){
                            transMessageBoHashMap.remove(transMessageBo);
                            transMessageBoList.remove(transMessageBo);
                            transMessageBo.getMessageVo().setStatus(Constant.MESSAGE_STATUS_FAIL);
                            ChattingTemper.getInstance().updateChattingVo(transMessageBo.getMessageVo());
                            DBManager.getInstance().updateMessageStatus(transMessageBo.getMessageVo());
                            EventBus.getDefault().post(new UpdateMessageEvent(transMessageBo.getMessageVo(), UpdateMessageEvent.MESSAGE_STATUS));
                            break;
                        }
                    }else if(transMessageBo.getSendCount() < MAX_SAND_TIMES && Common.getMilliDifferenceBetweenTime(transMessageBo.getLastTime()) > MAX_BETWEEN_TIME && transMessageBo.getStatus() == Constant.MESSAGE_STATUS_SENDING){
                        //执行重新发送逻辑
                        transMessageBo.setSendCount(transMessageBo.getSendCount() + 1);
                        transMessageBo.reSetLastTime();
                        MinaClienter.getInstance().sendMessage(Common.getMessageDtoFromMessageVo(transMessageBo.getMessageVo()));
                    }
                }
                try{
                    Thread.sleep(50);
                }catch(InterruptedException e){}

            }
        }
    }
}
