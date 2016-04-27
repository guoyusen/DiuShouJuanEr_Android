package com.bili.diushoujuaner.model.actionhelper.respon;

import com.bili.diushoujuaner.model.apihelper.ApiResponse;
import com.bili.diushoujuaner.utils.ConstantUtil;

/**
 * Created by BiLi on 2016/3/31.
 */
public class ActionResponse<T> {

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

    public static <T> ActionResponse<T> getActionRespon(String message, String retCode, T data){
        ActionResponse<T> actionResponse = new ActionResponse<>();

        actionResponse.setData(data);
        actionResponse.setMessage(message);
        actionResponse.setRetCode(retCode);

        return actionResponse;
    }

    public static <T> ActionResponse<T> getActionResponError(){
        ActionResponse<T> actionResponse = new ActionResponse<>();
        actionResponse.setData(null);
        actionResponse.setMessage("数据解析异常");
        actionResponse.setRetCode(ConstantUtil.RETCODE_ERROR);

        return actionResponse;
    }

    public static <T> ActionResponse<T> getActionRespon(T data){
        ActionResponse<T> actionResponse = new ActionResponse<>();
        actionResponse.setData(data);
        actionResponse.setMessage("本地数据获取成功");
        actionResponse.setRetCode(ConstantUtil.RETCODE_SUCCESS);

        return actionResponse;
    }

    public static <T> ActionResponse<T> getActionResponFromApiRespon(ApiResponse<T> value){
        ActionResponse<T> actionResponse = new ActionResponse<>();

        actionResponse.setData(value.getData());
        actionResponse.setMessage(value.getMessage());
        actionResponse.setRetCode(value.getRetCode());

        return actionResponse;
    }

}
