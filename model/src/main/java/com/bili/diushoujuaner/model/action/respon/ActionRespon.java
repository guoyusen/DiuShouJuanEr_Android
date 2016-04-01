package com.bili.diushoujuaner.model.action.respon;

import com.bili.diushoujuaner.model.apihelper.ApiRespon;

/**
 * Created by BiLi on 2016/3/31.
 */
public class ActionRespon<T> {

    private String message;
    private String retCode;
    private T data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRetCode() {
        return retCode;
    }

    public void setRetCode(String retCode) {
        this.retCode = retCode;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static <T> ActionRespon<T> getActionRespon(String message, String retCode, T data){
        ActionRespon<T> actionRespon = new ActionRespon<>();

        actionRespon.setData(data);
        actionRespon.setMessage(message);
        actionRespon.setRetCode(retCode);

        return actionRespon;
    }

    public static <T> ActionRespon<T> getActionRespon(ApiRespon<T> value){
        ActionRespon<T> actionRespon = new ActionRespon<>();

        actionRespon.setData(value.getData());
        actionRespon.setMessage(value.getMessage());
        actionRespon.setRetCode(value.getRetCode());

        return actionRespon;
    }

}
