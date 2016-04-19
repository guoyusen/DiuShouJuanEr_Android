package com.bili.diushoujuaner.model.tempHelper;

import java.util.Hashtable;

/**
 * Created by BiLi on 2016/3/23.
 */
public class GoodTemper {

    private Hashtable<Long, Boolean> booleanHashtable;
    private static GoodTemper goodTemper;

    public GoodTemper(){
        booleanHashtable = new Hashtable<>();
    }

    public static synchronized GoodTemper getInstance(){
        if(goodTemper == null){
            goodTemper = new GoodTemper();
        }
        return goodTemper;
    }

    public void setGoodStatus(Long recallNo, Boolean value){
        booleanHashtable.put(recallNo, value);
    }

    public boolean isExists(Long recallNo){
        return booleanHashtable.get(recallNo) != null;
    }

    public boolean getGoodStatus(Long recallNo){
        return booleanHashtable.get(recallNo) != null ? booleanHashtable.get(recallNo) : false;
    }

    public void clear(){
        booleanHashtable.clear();
    }

}
