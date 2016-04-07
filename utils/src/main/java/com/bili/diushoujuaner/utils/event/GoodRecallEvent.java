package com.bili.diushoujuaner.utils.event;

/**
 * Created by BiLi on 2016/4/1.
 */
public class GoodRecallEvent {

    private int position;

    public GoodRecallEvent(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
