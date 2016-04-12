package com.bili.diushoujuaner.presenter.event;

/**
 * Created by BiLi on 2016/4/1.
 */
public class GoodRecallEvent {

    private int position;
    private int type;
    private int index;

    public GoodRecallEvent(int index, int position, int type) {
        this.index = index;
        this.position = position;
        this.type = type;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
