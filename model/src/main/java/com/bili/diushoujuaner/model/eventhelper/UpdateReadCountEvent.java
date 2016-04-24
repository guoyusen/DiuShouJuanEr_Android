package com.bili.diushoujuaner.model.eventhelper;

/**
 * Created by BiLi on 2016/4/19.
 */
public class UpdateReadCountEvent {

    private int type;
    private int count;

    public UpdateReadCountEvent(int type, int count) {
        this.type = type;
        this.count = count;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
