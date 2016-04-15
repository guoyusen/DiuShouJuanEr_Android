package com.bili.diushoujuaner.model.eventhelper;

/**
 * Created by BiLi on 2016/4/10.
 */
public class RemoveRecallEvent {

    private int position;
    private Long recallNo;
    private int index;

    public RemoveRecallEvent(int index, int position, Long recallNo) {
        this.index = index;
        this.position = position;
        this.recallNo = recallNo;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public Long getRecallNo() {
        return recallNo;
    }

    public void setRecallNo(Long recallNo) {
        this.recallNo = recallNo;
    }

}
