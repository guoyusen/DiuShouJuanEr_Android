package com.bili.diushoujuaner.model.tempHelper;

import android.util.Log;

import com.bili.diushoujuaner.utils.Common;
import com.bili.diushoujuaner.utils.Constant;
import com.bili.diushoujuaner.utils.entity.bo.CurrentChatBo;
import com.bili.diushoujuaner.utils.entity.vo.ChattingVo;
import com.bili.diushoujuaner.utils.entity.vo.MessageVo;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * Created by BiLi on 2016/4/13.
 */
public class ChattingTemper {

    private Hashtable<String,ChattingVo> hashtable;
    private List<ChattingVo> chattingVoList;
    private CurrentChatBo currentChat;
    private List<OnUpdateMessageListener> messageListenerList;
    private static ChattingTemper chattingTemper;

    public ChattingTemper(){
        hashtable = new Hashtable<>();
        chattingVoList = new ArrayList<>();
        currentChat = new CurrentChatBo();
        messageListenerList = new ArrayList<>();
    }

    public static synchronized ChattingTemper getInstance(){
        if(chattingTemper == null){
            chattingTemper = new ChattingTemper();
        }
        return chattingTemper;
    }

    public static int getUnReadCount(){
        int unReadCount = 0;
        for(ChattingVo chattingVo : ChattingTemper.getInstance().getChattingVoList()){
            unReadCount += chattingVo.getUnReadCount();
        }
        return unReadCount;
    }


    public interface OnUpdateMessageListener{
        void onUpdateMessage(MessageVo messageVo);
    }

    public void clear(){
        hashtable.clear();
        chattingVoList.clear();
        messageListenerList.clear();
        resetCurrentChatBo();
    }

    public void setCurrentChatBo(long userNo, int msgType){
        currentChat.setToNo(userNo);
        currentChat.setMsgType(msgType);
        currentChat.setChatting(true);
    }

    public void resetCurrentChatBo(){
        currentChat.setToNo(-1);
        currentChat.setMsgType(-1);
        currentChat.setChatting(false);
    }

    public List<ChattingVo> getChattingVoList(){
        return chattingVoList;
    }

    public boolean isChatting(){
        return currentChat.isChatting();
    }

    public int getMsgType() {
        return currentChat.getMsgType();
    }

    public long getToNo() {
        return currentChat.getToNo();
    }

    public void registerMessageListener(OnUpdateMessageListener messageListener){
        messageListenerList.add(messageListener);
    }

    public void unRegisterMessageListener(OnUpdateMessageListener messageListener){
        messageListenerList.remove(messageListener);
    }

    public void moveToFirst(int position){
        if(position > chattingVoList.size() - 1){
            return;
        }
        ChattingVo chattingVo = chattingVoList.remove(position);
        chattingVoList.add(0, chattingVo);
    }

    /**
     * 将消息通知给监听者
     * @param messageVo
     */
    public void publishToListener(MessageVo messageVo){
        //通知所有的监听者
        //正在聊天界面
        //聊天类型相同
        //群聊或者单聊
        if(isChatting() && getMsgType() == messageVo.getMsgType() &&
                ((getMsgType() == Constant.CHAT_PAR && getToNo() == messageVo.getToNo()) ||
                        getMsgType() == Constant.CHAT_FRI && getToNo() == messageVo.getFromNo())){
            for(OnUpdateMessageListener messageListener : messageListenerList){
                messageListener.onUpdateMessage(messageVo);
            }
        }
    }

    /**
     * 在主动发送的时候，调用该方法判断是否需要显示时间
     * @param messageVo
     * @return
     */
    public boolean getIsNeedShowTime(MessageVo messageVo){
        String key = getKey(messageVo, true);
        ChattingVo chattingVo = hashtable.get(key);
        if(chattingVo != null){
            return Common.getMinuteDifferenceBetweenTime(chattingVo.getLastShowTime(), messageVo.getTime()) > 5;
        }
        return true;
    }

    public void updateChattingVoRead(long userNo, int msgType){
        ChattingVo chattingVo = hashtable.get(userNo + "_" + msgType);
        if(chattingVo != null){
            chattingVo.setUnReadCount(0);
        }
    }

    public void deleteChattingVo(long userNo, int msgType){
        chattingVoList.remove(hashtable.remove(userNo + "_" + msgType));
    }

    /**
     * 更新消息的发送状态，该方法只有在messageSent方法中回调使用
     */
    public void updateChattingVo(MessageVo messageVo){
        if(messageVo == null){
            return;
        }
        String key = getKey(messageVo, false);
        ChattingVo chattingVo = hashtable.get(key);
        if(chattingVo == null || !chattingVo.getSerialNo().equals(messageVo.getSerialNo())){
            return;
        }
        //消息发送成功
        chattingVo.setStatus(messageVo.getStatus());
    }

    public void addChattingVoFromServer(MessageVo messageVo){
        addChattingVo(messageVo, true);
    }

    public void addChattingVoFromLocal(MessageVo messageVo){
        addChattingVo(messageVo, false);
    }

    /**
     * 发送新消息，接收新消息时使用
     */
    private void addChattingVo(MessageVo messageVo, boolean fromServer){
        if(messageVo == null){
            return;
        }
        if(fromServer){
            setReadable(messageVo);
        }
        String key = getKey(messageVo, fromServer);
        ChattingVo chattingVo = hashtable.get(key);
        if(chattingVo != null){
            hashtable.remove(key);
            chattingVoList.remove(chattingVo);
            if(fromServer){
                messageVo.setTimeShow(Common.getMinuteDifferenceBetweenTime(chattingVo.getLastShowTime(), messageVo.getTime()) > 5);
            }
            chattingVo.setLastShowTime(messageVo.getTime());
            if((currentChat.isChatting() && (currentChat.getToNo() == (messageVo.getMsgType() == Constant.CHAT_FRI ? messageVo.getFromNo() : messageVo.getToNo()) && currentChat.getMsgType() == messageVo.getMsgType()))){
                //正在聊天页面，且当前聊天的正好是接收人
                chattingVo.setUnReadCount(0);
            }else if(!messageVo.isRead()){
                chattingVo.setUnReadCount(chattingVo.getUnReadCount() + 1);
            }
        }else{
            chattingVo = new ChattingVo();
            if(fromServer){
                messageVo.setTimeShow(true);
            }
            chattingVo.setLastShowTime(messageVo.getTime());
            chattingVo.setUnReadCount(messageVo.isRead() ? 0 : 1);
            if(fromServer){
                chattingVo.setUserNo(messageVo.getMsgType() == Constant.CHAT_FRI ? messageVo.getFromNo() : messageVo.getToNo());
            }else{
                chattingVo.setUserNo(messageVo.getToNo());
            }
        }
        if(messageVo.getMsgType() == Constant.CHAT_PAR){
            chattingVo.setMemberNo(messageVo.getFromNo());
        }
        chattingVo.setSerialNo(messageVo.getSerialNo());
        chattingVo.setMsgType(messageVo.getMsgType());
        chattingVo.setContent(messageVo.getContent());
        chattingVo.setConType(messageVo.getConType());
        chattingVo.setStatus(messageVo.getStatus());
        chattingVo.setTime(messageVo.getTime());

        hashtable.put(key, chattingVo);
        chattingVoList.add(0, chattingVo);
    }

    private void setReadable(MessageVo messageVo){
        //正在聊天界面
        //聊天类型相同
        //群聊或者单聊
        if(ChattingTemper.getInstance().isChatting() && ChattingTemper.getInstance().getMsgType() == messageVo.getMsgType()
                && ChattingTemper.getInstance().getToNo() == messageVo.getToNo()){
            messageVo.setRead(true);
        }else{
            messageVo.setRead(false);
        }
    }

    private static String getKey(MessageVo messageVo, boolean fromServer){
        String key;
        if(fromServer){//服务端发过来的数据
            if(messageVo.getMsgType() == Constant.CHAT_FRI){
                key = messageVo.getFromNo() + "_" + messageVo.getMsgType();
            }else{
                key = messageVo.getToNo() + "_" + messageVo.getMsgType();
            }
        }else{
            key = messageVo.getToNo() + "_" + messageVo.getMsgType();
        }

        return key;
    }

    public void removeChattingVo(String key){
        chattingVoList.remove(hashtable.remove(key));
    }

}
