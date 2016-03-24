package com.bili.diushoujuaner.model.tempHelper;

import java.util.Hashtable;

/**
 * Created by BiLi on 2016/3/23.
 */
public class GoodTemper {

    private static Hashtable<Long, Boolean> booleanHashtable = new Hashtable<>();

    public static void setGoodStatus(Long recallNo, Boolean value){
        booleanHashtable.put(recallNo, value);
    }

    public static boolean isExists(Long recallNo){
        if(booleanHashtable.get(recallNo) != null){
            return true;
        }
        return false;
    }

    public static boolean getGoodStatus(Long recallNo){
        if(booleanHashtable.get(recallNo) != null){
            return booleanHashtable.get(recallNo);
        }
        return false;
    }

    public static void clear(){
        booleanHashtable.clear();
    }

}
