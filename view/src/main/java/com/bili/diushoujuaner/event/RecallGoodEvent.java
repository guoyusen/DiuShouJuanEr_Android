package com.bili.diushoujuaner.event;

/**
 * Created by BiLi on 2016/4/1.
 */
public class RecallGoodEvent {

    private int position;

    public RecallGoodEvent(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
