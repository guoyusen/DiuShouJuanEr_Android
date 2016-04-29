package com.bili.diushoujuaner.model.messagehelper;

import com.bili.diushoujuaner.model.databasehelper.DBManager;
import com.bili.diushoujuaner.model.preferhelper.HomeStatePreference;
import com.bili.diushoujuaner.utils.ConstantUtil;
import com.bili.diushoujuaner.utils.NotificationUtil;
import com.bili.diushoujuaner.utils.entity.vo.MessageVo;

/**
 * Created by BiLi on 2016/4/28.
 */
public class Notifier {

    public static void showNotification(MessageVo messageVo){
        if(HomeStatePreference.getInstance().isBackground()){
            NotificationUtil.getInstance().showNotice(getKey(messageVo), getUserName(messageVo), messageVo.getContent());
        }
    }

    public static void clear(){
        NotificationUtil.getInstance().clear();
    }

    private static String getUserName(MessageVo messageVo){
        if(messageVo.getMsgType() == ConstantUtil.CHAT_FRI){
            return DBManager.getInstance().getUser(messageVo.getFromNo()).getNickName();
        }else if(messageVo.getMsgType() == ConstantUtil.CHAT_PAR){
            return DBManager.getInstance().getParty(messageVo.getToNo()).getPartyName();
        }else{
            return "";
        }
    }

    private static String getKey(MessageVo messageVo){
        if(messageVo.getMsgType() == ConstantUtil.CHAT_FRI){
            return messageVo.getFromNo() + "_" + messageVo.getMsgType();
        }else if(messageVo.getMsgType() == ConstantUtil.CHAT_PAR){
            return messageVo.getToNo() + "_" + messageVo.getMsgType();
        }else{
            return "NONE";
        }
    }

}
