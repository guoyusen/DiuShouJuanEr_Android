package com.bili.diushoujuaner.model;


import com.bili.diushoujuaner.model.callback.IDataCallback;
import com.bili.diushoujuaner.model.entities.UserDto;

/**
 * Created by BiLi on 2016/3/10.
 */
public class UserModel {

    private static UserModel userModel;

    public static UserModel getInstance(){
        if(userModel == null){
            userModel = new UserModel();
        }
        return userModel;
    }

    public void getUserLogin(String mobile, String psd, IDataCallback<UserDto> iDataCallback){



    }

}
