package com.bili.diushoujuaner.model;

import com.bili.diushoujuaner.model.callback.IDataCallback;
import com.bili.diushoujuaner.model.entities.RecallVo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BiLi on 2016/3/10.
 */
public class RecallModel {

    private static RecallModel recallModel;

    public static RecallModel getInstance(){
        if(recallModel == null){
            recallModel = new RecallModel();
        }
        return recallModel;
    }

    public void getRecallList(IDataCallback<List<RecallVo>> iDataCallback){

        List<RecallVo> list = new ArrayList<>();
        for(int i=0;i<9;i++){
            list.add(new RecallVo());
        }
        iDataCallback.onSuccess(list);

    }

}
