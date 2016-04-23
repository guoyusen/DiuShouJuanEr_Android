package com.bili.diushoujuaner.presenter.messager;

import android.util.Log;

import com.bili.diushoujuaner.model.databasehelper.DBManager;
import com.bili.diushoujuaner.utils.Common;
import com.bili.diushoujuaner.utils.Constant;
import com.bili.diushoujuaner.utils.entity.bo.TransMessageBo;
import com.bili.diushoujuaner.utils.entity.vo.MessageVo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by BiLi on 2016/4/17.
 */
public class Transceiver extends Thread {

    private HashMap<String, TransMessageBo> transMessageBoHashMap;
    final private List<TransMessageBo> transMessageBoList;
    private static final int MAX_SAND_TIMES = 3;//最大重发次数
    private static final int MAX_BETWEEN_TIME = 5000;//超过5000毫秒没有接收到则认为发送失败
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

            MinaClienter.getInstance().sendMessage(Common.getMessageDtoFromMessageVo(messageVo));
            transMessageBoList.notify();
        }
    }

    public void updateStatusSuccess(String serialNo){
        synchronized (transMessageBoList){
            Log.d("guoyusenmm","更新状态");
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
                executeMessage();
            }
            try{
                //数量多少来确定循环的间隔时间，多则少，少则多
                Thread.sleep(100 / (transMessageBoList.size() > 0 ? transMessageBoList.size() : 1));
            }catch(InterruptedException e){}
        }
    }

    private void executeMessage(){
        Log.d("guoyusenm","收发器循环");
        for(TransMessageBo transMessageBo : transMessageBoList){
            if(transMessageBo.getStatus() == Constant.MESSAGE_STATUS_SUCCESS){
                //发送成功，执行通知，更新界面，更新数据库，删除该消息在收发器中的存储
                transMessageBoHashMap.remove(transMessageBo);
                transMessageBoList.remove(transMessageBo);
                if(transMessageBo.getMessageVo().getMsgType() == Constant.CHAT_INIT){
                    MessageServiceHandler.getInstance().sendMessageToClient(Constant.HANDLER_LOGIN, null);
                }else if(transMessageBo.getMessageVo().getMsgType() == Constant.CHAT_PAR || transMessageBo.getMessageVo().getMsgType() == Constant.CHAT_FRI){
                    transMessageBo.getMessageVo().setStatus(Constant.MESSAGE_STATUS_SUCCESS);
                    DBManager.getInstance().updateMessageStatus(transMessageBo.getMessageVo());
                    MessageServiceHandler.getInstance().sendMessageToClient(Constant.HANDLER_STATUS, transMessageBo.getMessageVo());
                }
                break;
            }else if(transMessageBo.getSendCount() >= MAX_SAND_TIMES && Common.getMilliDifferenceBetweenTime(transMessageBo.getLastTime()) > MAX_BETWEEN_TIME && transMessageBo.getStatus() == Constant.MESSAGE_STATUS_SENDING){
                //发送已经达到3次，相聚上次时间间隔超过1000毫秒，状态还是发送中
                transMessageBoHashMap.remove(transMessageBo);
                transMessageBoList.remove(transMessageBo);
                if(transMessageBo.getMessageVo().getMsgType() == Constant.CHAT_PAR || transMessageBo.getMessageVo().getMsgType() == Constant.CHAT_FRI){
                    transMessageBo.getMessageVo().setStatus(Constant.MESSAGE_STATUS_FAIL);
                    DBManager.getInstance().updateMessageStatus(transMessageBo.getMessageVo());
                    MessageServiceHandler.getInstance().sendMessageToClient(Constant.HANDLER_STATUS, transMessageBo.getMessageVo());
                }
                break;
            }else if(transMessageBo.getSendCount() < MAX_SAND_TIMES && Common.getMilliDifferenceBetweenTime(transMessageBo.getLastTime()) > MAX_BETWEEN_TIME && transMessageBo.getStatus() == Constant.MESSAGE_STATUS_SENDING){
                //执行重新发送逻辑
                Log.d("guoyusenmm","重新发送");
                transMessageBo.setSendCount(transMessageBo.getSendCount() + 1);
                transMessageBo.reSetLastTime();
                MinaClienter.getInstance().sendMessage(Common.getMessageDtoFromMessageVo(transMessageBo.getMessageVo()));
            }
        }
    }
}
