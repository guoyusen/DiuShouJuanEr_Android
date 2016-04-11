package com.bili.diushoujuaner.model.apihelper.request;

/**
 * Created by BiLi on 2016/4/11.
 */
public class HeadUpdateReq {

    private String path;
    private String key;

    public HeadUpdateReq(String key, String path) {
        this.key = key;
        this.path = path;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
