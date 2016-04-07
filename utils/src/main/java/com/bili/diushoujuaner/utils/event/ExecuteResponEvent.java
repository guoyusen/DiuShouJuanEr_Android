package com.bili.diushoujuaner.utils.event;

/**
 * Created by BiLi on 2016/4/1.
 */
public class ExecuteResponEvent {

    private Long commentNo;
    private Long responNo;
    private long toNo;
    private int type;
    private String nickName;

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Long getCommentNo() {
        return commentNo;
    }

    public void setCommentNo(Long commentNo) {
        this.commentNo = commentNo;
    }

    public Long getResponNo() {
        return responNo;
    }

    public void setResponNo(Long responNo) {
        this.responNo = responNo;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getToNo() {
        return toNo;
    }

    public void setToNo(long toNo) {
        this.toNo = toNo;
    }

    public static ExecuteResponEvent getResponEvent(Long commentNo, Long responNo, Long toNo, int type, String nickName){
        ExecuteResponEvent executeResponEvent = new ExecuteResponEvent();
        if(commentNo != null){
            executeResponEvent.setCommentNo(commentNo);
        }
        if(responNo != null){
            executeResponEvent.setResponNo(responNo);
        }
        executeResponEvent.setToNo(toNo);
        executeResponEvent.setType(type);
        executeResponEvent.setNickName(nickName);

        return executeResponEvent;
    }
}
