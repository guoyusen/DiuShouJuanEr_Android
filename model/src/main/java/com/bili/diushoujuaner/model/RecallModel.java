package com.bili.diushoujuaner.model;

import com.bili.diushoujuaner.utils.response.RecallVo;
import com.bili.diushoujuaner.model.callback.ActionCallbackListener;

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

    public void getRecallList(ActionCallbackListener<List<RecallVo>> actionCallbackListener){

        List<RecallVo> list = new ArrayList<>();
        for(int i=0;i<9;i++){
            list.add(new RecallVo());
        }
        actionCallbackListener.onSuccess(list);

    }

}
