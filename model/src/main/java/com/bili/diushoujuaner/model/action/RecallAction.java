package com.bili.diushoujuaner.model.action;

import com.bili.diushoujuaner.utils.response.RecallVo;
import com.bili.diushoujuaner.model.callback.ActionCallbackListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BiLi on 2016/3/10.
 */
public class RecallAction {

    private static RecallAction recallAction;

    public static RecallAction getInstance(){
        if(recallAction == null){
            recallAction = new RecallAction();
        }
        return recallAction;
    }

    public void getRecallList(ActionCallbackListener<List<RecallVo>> actionCallbackListener){

        List<RecallVo> list = new ArrayList<>();
        for(int i=0;i<9;i++){
            list.add(new RecallVo());
        }
        actionCallbackListener.onSuccess(list);

    }

}
