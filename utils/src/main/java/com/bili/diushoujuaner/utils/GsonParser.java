package com.bili.diushoujuaner.utils;

import com.google.gson.Gson;

/**
 * Created by BiLi on 2016/3/13.
 */
public class GsonParser {

    private static Gson gson;

    public static synchronized Gson getInstance(){
        if(gson == null){
            gson = new Gson();
        }
        return gson;
    }

}
