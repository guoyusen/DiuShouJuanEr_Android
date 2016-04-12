package com.bili.diushoujuaner.presenter.event;

/**
 * Created by BiLi on 2016/4/6.
 */
public class UpdateWallPaperEvent {

    private String path;

    public UpdateWallPaperEvent(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
