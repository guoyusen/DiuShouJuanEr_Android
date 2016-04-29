package com.bili.diushoujuaner.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;
import android.widget.Toast;

import com.bili.diushoujuaner.utils.entity.vo.MessageVo;

import java.util.HashMap;
import java.util.List;

/**
 * Created by BiLi on 2016/4/28.
 */
public class NotificationUtil {

    /**
     * 通知类型：
     * 1.好友消息
     * 2.群消息
     */

    private static NotificationUtil notificationUtil;
    private NotificationManager notificationManager;
    private NotificationCompat.Builder builder;
    private Class forwardClass;
    private int icon;
    private String title;
    private String content;
    private HashMap<String, Integer> msgCount;
    private Context context;

    public NotificationUtil(Context context, Class clzss, int icon) {
        this.context = context;
        this.notificationManager = (NotificationManager) context.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
        this.builder = new NotificationCompat.Builder(context);
        this.forwardClass = clzss;
        this.icon = icon;
        this.msgCount = new HashMap<>();
        this.title = "";
        this.content = "";
    }

    public static void init(Context context, Class clzss, int icon){
        if(notificationUtil == null){
            notificationUtil = new NotificationUtil(context, clzss, icon);
        }
    }

    public synchronized static NotificationUtil getInstance(){
        if(notificationUtil == null){
            throw new RuntimeException("NotificationUtil was not init.");
        }
        return notificationUtil;
    }

    public void clear(){
        notificationManager.cancel(0);
        msgCount.clear();
        this.title = "";
        this.content = "";
    }

    private synchronized void updateMsgCount(String key){
        if(msgCount.get(key) == null){
            msgCount.put(key, 1);
        }else{
            msgCount.put(key, msgCount.get(key) + 1);
        }
    }

    private int getMsgCount(){
        int count = 0;
        for(Integer item : msgCount.values()){
            count += item;
        }
        return count;
    }

    public void showNotice(String key, String userName, String content){
        this.title = "";
        this.content = "";
        updateMsgCount(key);
        if(msgCount.size() == 1){
            this.title = userName;
            if(msgCount.get(key) > 1){
                this.title += " (" + msgCount.get(key) + "条新消息)";
            }
            this.content = content;
        }else if(msgCount.size() > 1){
            this.title = "丢手绢儿";
            this.content = msgCount.size() + "个联系人发来" + getMsgCount() + "条消息";
        }else{
            return;
        }
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, new Intent(context, forwardClass), PendingIntent.FLAG_UPDATE_CURRENT);
        try{

            builder.setAutoCancel(true)
                    .setContentTitle(this.title)
                    .setContentText(this.content)
                    .setSmallIcon(icon)
                    .setContentIntent(contentIntent)
                    .setDefaults(Notification.DEFAULT_ALL);
            notificationManager.notify(0, builder.build());
        }catch(Exception e){
            Toast.makeText(context, e.toString(),Toast.LENGTH_LONG).show();
        }
    }


}
