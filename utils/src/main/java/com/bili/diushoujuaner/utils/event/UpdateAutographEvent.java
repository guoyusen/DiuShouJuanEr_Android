package com.bili.diushoujuaner.utils.event;

/**
 * Created by BiLi on 2016/4/6.
 */
public class UpdateAutographEvent {

    private String autograph;

    public UpdateAutographEvent(String autograph) {
        this.autograph = autograph;
    }

    public String getAutograph() {
        return autograph;
    }

    public void setAutograph(String autograph) {
        this.autograph = autograph;
    }
}
