package com.bili.diushoujuaner.utils;

import android.content.Context;
import android.media.MediaPlayer;

/**
 * Created by BiLi on 2016/4/16.
 */
public class NoticeUtil {

    private MediaPlayer player;
    private Context context;
    private int audioId;
    private static NoticeUtil noticeUtil;

    public NoticeUtil(Context context, int audioId) {
        this.context = context;
        this.audioId = audioId;
        player = MediaPlayer.create(context, audioId);
    }

    public static void init(Context context, int audioId){
        if(noticeUtil == null){
            noticeUtil = new NoticeUtil(context, audioId);
        }
    }

    public static synchronized NoticeUtil getInstance(){
        if(noticeUtil == null){
            throw new RuntimeException("消息通知没有初始化");
        }
        return noticeUtil;
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
