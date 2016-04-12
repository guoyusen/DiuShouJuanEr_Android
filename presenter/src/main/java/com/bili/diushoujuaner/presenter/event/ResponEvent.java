package com.bili.diushoujuaner.presenter.event;

/**
 * Created by BiLi on 2016/4/1.
 */
public class ResponEvent {

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

    public static ResponEvent getResponEvent(Long commentNo, Long responNo, Long toNo, int type, String nickName){
        ResponEvent responEvent = new ResponEvent();
        if(commentNo != null){
            responEvent.setCommentNo(commentNo);
        }
        if(responNo != null){
            responEvent.setResponNo(responNo);
        }
        responEvent.setToNo(toNo);
        responEvent.setType(type);
        responEvent.setNickName(nickName);

        return responEvent;
    }
}
