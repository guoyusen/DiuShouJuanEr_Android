package com.bili.diushoujuaner.utils;

import android.content.Context;
import android.media.MediaPlayer;

/**
 * Created by BiLi on 2016/4/16.
 */
public class MessageNoticer {

    private MediaPlayer player;
    private Context context;
    private int audioId;
    private static MessageNoticer messageNoticer;

    public MessageNoticer(Context context, int audioId) {
        this.context = context;
        this.audioId = audioId;
        player = MediaPlayer.create(context, audioId);
    }

    public static void init(Context context, int audioId){
        if(messageNoticer == null){
            messageNoticer = new MessageNoticer(context, audioId);
        }
    }

    public static synchronized MessageNoticer getInstance(){
        if(messageNoticer == null){
            throw new RuntimeException("消息通知没有初始化");
        }
        return messageNoticer;
    }

    public void playNotice(){
        if(player == null){
            return;
        }
        player.start();
    }

    public void dispose(){
        if(player == null){
            return;
        }
        player.stop();
        player.release();
        player=null;
    }

}
