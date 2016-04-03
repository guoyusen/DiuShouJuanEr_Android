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
        return booleanHashtable.get(recallNo) != null;
    }

    public static boolean getGoodStatus(Long recallNo){
        return booleanHashtable.get(recallNo) != null ? booleanHashtable.get(recallNo) : false;
    }

    public static void clear(){
        booleanHashtable.clear();
    }

}
