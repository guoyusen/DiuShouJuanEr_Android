package com.bili.diushoujuaner.model.apihelper;

import com.bili.diushoujuaner.utils.ConstantUtil;

import java.io.Serializable;

/**
 * Created by BiLi on 2016/3/11.
 */
public class ApiRespon<T>  implements Serializable {

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

    public boolean getIsLegal(){
        return this.getRetCode().equals(ConstantUtil.RETCODE_SUCCESS);
    }

}
