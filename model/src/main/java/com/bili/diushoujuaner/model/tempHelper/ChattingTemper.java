package com.bili.diushoujuaner.model.tempHelper;

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

    private static Hashtable<String,ChattingVo> hashtable = new Hashtable<>();
    private static List<ChattingVo> chattingVoList = new ArrayList<>();
    private static CurrentChatBo currentChat = new CurrentChatBo();
    private static List<OnUpdateMessageListener> messageListenerList = new ArrayList<>();

    public interface OnUpdateMessageListener{
        void onUpdateMessage(MessageVo messageVo);
    }

    public static void clear(){
        hashtable.clear();
        chattingVoList.clear();
        messageListenerList.clear();
        resetCurrentChatBo();
    }

    public static void setCurrentChatBo(long userNo, int msgType){
        currentChat.setUserNo(userNo);
        currentChat.setMsgType(msgType);
        currentChat.setChatting(true);
    }

    public static void resetCurrentChatBo(){
        currentChat.setUserNo(-1);
        currentChat.setMsgType(-1);
        currentChat.setChatting(false);
    }

    public static List<ChattingVo> getChattingVoList(){
        return chattingVoList;
    }

    public static boolean isChatting(){
        return currentChat.isChatting();
    }

    public static int getMsgType() {
        return currentChat.getMsgType();
    }

    public static long getUserNo() {
        return currentChat.getUserNo();
    }

    public static void registerMessageListener(OnUpdateMessageListener messageListener){
        messageListenerList.add(messageListener);
    }

    public static void unRegisterMessageListener(OnUpdateMessageListener messageListener){
        messageListenerList.remove(messageListener);
    }

    public static void moveToFirst(int position){
        if(position > chattingVoList.size() - 1){
            return;
        }
        ChattingVo chattingVo = chattingVoList.remove(position);
        chattingVoList.add(0, chattingVo);
    }

    public static void addChattingVo(MessageVo messageVo){
        //默认顺序叠加就可以，当正常通信过程中，则添加到首位
        addChattingVo(messageVo, false);
    }

    public static void publishToListener(MessageVo messageVo){
        //通知所有的监听者
        //正在聊天界面
        //聊天类型相同
        //群聊或者单聊
        if(ChattingTemper.isChatting() && ChattingTemper.getMsgType() == messageVo.getMsgType() &&
                ((ChattingTemper.getMsgType() == Constant.CHAT_PAR && ChattingTemper.getUserNo() == messageVo.getToNo()) ||
                        ChattingTemper.getMsgType() == Constant.CHAT_FRI && ChattingTemper.getUserNo() == messageVo.getFromNo())){
            for(OnUpdateMessageListener messageListener : messageListenerList){
                messageListener.onUpdateMessage(messageVo);
            }
        }
    }

    public static void addChattingVo(MessageVo messageVo, boolean insertIntoFirst){
        ChattingVo chattingVo;
        String key = "";
        //做如此区分，防止群号和好友号一致时导致数据有误
        if(messageVo.getMsgType() == Constant.CHAT_FRI){
            //好友类型  好友账号+好友聊天类型
            key = messageVo.getFromNo() + "_" + messageVo.getMsgType();
        }else if(messageVo.getMsgType() == Constant.CHAT_PAR){
            //群聊类型  群账号+群聊类型
            key = messageVo.getToNo() + "_" + messageVo.getMsgType();
        }
        chattingVo = hashtable.get(key);
        if(chattingVo != null){
            if(Common.getMinuteDifferenceBetweenTime(chattingVo.getLastShowTime(), messageVo.getTime()) > 5){
                messageVo.setTimeShow(true);
                chattingVo.setLastShowTime(messageVo.getTime());
            }else{
                messageVo.setTimeShow(false);
            }
            if((currentChat.isChatting() && (currentChat.getUserNo() == (messageVo.getMsgType() == Constant.CHAT_FRI ? messageVo.getFromNo() : messageVo.getToNo()) && currentChat.getMsgType() == messageVo.getMsgType()))){
                //正在聊天页面，且当前聊天的正好是接收人
                chattingVo.setUnReadCount(0);
            }else if(!messageVo.isRead()){
                chattingVo.setUnReadCount(chattingVo.getUnReadCount() + 1);
            }
        }else{
            chattingVo = new ChattingVo();
            messageVo.setTimeShow(true);
            chattingVo.setLastShowTime(messageVo.getTime());
            if(messageVo.isRead()){
                chattingVo.setUnReadCount(0);
            }else{
                chattingVo.setUnReadCount(1);
            }
            if(messageVo.getMsgType() == Constant.CHAT_FRI){
                //好友类型  好友账号+好友聊天类型
                chattingVo.setUserNo(messageVo.getFromNo());
            }else if(messageVo.getMsgType() == Constant.CHAT_PAR){
                //群聊类型  群账号+群聊类型
                chattingVo.setUserNo(messageVo.getToNo());
            }
        }
        if(messageVo.getMsgType() == Constant.CHAT_PAR){
            chattingVo.setMemberNo(messageVo.getFromNo());
        }
        chattingVo.setMsgType(messageVo.getMsgType());
        chattingVo.setContent(messageVo.getContent());
        chattingVo.setConType(messageVo.getConType());
        chattingVo.setStatus(messageVo.getStatus());
        chattingVo.setTime(messageVo.getTime());

        if(hashtable.get(key) == null){
            hashtable.put(key, chattingVo);
            if(insertIntoFirst){
                chattingVoList.add(0, chattingVo);
            }else{
                chattingVoList.add(chattingVo);
            }
        }else{
            hashtable.put(key, chattingVo);
            if(insertIntoFirst){
                chattingVoList.remove(chattingVo);
                chattingVoList.add(0, chattingVo);
            }
        }

    }

    public static void removeChattingVo(String key){
        chattingVoList.remove(hashtable.remove(key));
    }

}
